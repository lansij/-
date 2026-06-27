package com.psychology.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psychology.feign.UserFeignClient;
import com.psychology.pojo.Comment;
import com.psychology.pojo.User;
import com.psychology.service.CommentService;
import com.psychology.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserFeignClient userFeignClient;

    //添加评论
    @PostMapping("/addComment")
    public boolean addComment(@RequestBody Comment comment) {
        if (comment.getStatus() == null) {
            comment.setStatus(1);
        }
        return commentService.save(comment);
    }

    //修改评论
    @PostMapping("/updateComment")
    public boolean updateComment(@RequestBody Comment comment) {
        return commentService.updateById(comment);
    }

    //根据id删除
    @PostMapping("/deleteCommentById/{id}")
    public boolean deleteCommentById(@PathVariable("id") Long id) {
        return commentService.removeById(id);
    }

    //批量删除
    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list) {
        return commentService.removeBatchByIds(list);
    }

    //根据id查询
    @PostMapping("/getCommentById/{id}")
    public Comment getCommentById(@PathVariable("id") Long id) {
        return commentService.getById(id);
    }

    //根据用户id查询评论
    @PostMapping("/getCommentByUserId/{userId}")
    public List<Comment> getCommentByUserId(@PathVariable("userId") Long userId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("status", 1);
        wrapper.orderByDesc("create_time");
        return commentService.list(wrapper);
    }

    //根据目标查询评论
    @PostMapping("/getCommentByTarget")
    public List<Comment> getCommentByTarget(@RequestBody Map<String, Object> map) {
        String targetType = (String) map.get("targetType");
        Long targetId = Long.valueOf(map.get("targetId").toString());
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("target_type", targetType);
        wrapper.eq("target_id", targetId);
        wrapper.eq("status", 1);
        wrapper.orderByDesc("create_time");
        return commentService.list(wrapper);
    }

    //根据目标查询评论（GET，包含用户昵称和头像）
    @GetMapping("/list")
    public List<Map<String, Object>> list(@RequestParam String targetType, @RequestParam Long targetId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("target_type", targetType);
        wrapper.eq("target_id", targetId);
        wrapper.eq("status", 1);
        wrapper.orderByDesc("create_time");
        List<Comment> comments = commentService.list(wrapper);
        // 包装用户信息
        List<Map<String, Object>> result = new ArrayList<>();
        for (Comment c : comments) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("userId", c.getUserId());
            item.put("targetType", c.getTargetType());
            item.put("targetId", c.getTargetId());
            item.put("content", c.getContent());
            item.put("createTime", c.getCreateTime());
            // 通过Feign调用用户服务查询用户信息
            try {
                User user = userFeignClient.getUserById(c.getUserId());
                if (user != null) {
                    item.put("nickname", user.getNickname());
                    item.put("avatar", user.getAvatar());
                } else {
                    item.put("nickname", "匿名用户");
                    item.put("avatar", null);
                }
            } catch (Exception e) {
                item.put("nickname", "匿名用户");
                item.put("avatar", null);
            }
            result.add(item);
        }
        return result;
    }

    //添加评论（POST /add，从token解析userId）
    @PostMapping("/add")
    public boolean add(@RequestBody Comment comment,
                       @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                String decoded = new String(java.util.Base64.getDecoder().decode(token));
                Long userId = Long.valueOf(decoded.split(":")[0]);
                comment.setUserId(userId);
            } catch (Exception ignored) {
            }
        }
        if (comment.getStatus() == null) {
            comment.setStatus(1);
        }
        return commentService.save(comment);
    }

    //所有评论列表（GET，分页，管理员用）
    @GetMapping("/allList")
    public Map<String, Object> allList(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        Page<Comment> p = new Page<>(page, size);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        commentService.page(p, wrapper);
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("records", p.getRecords());
        pageData.put("total", p.getTotal());
        Map<String, Object> result = new HashMap<>();
        result.put("data", pageData);
        return result;
    }

    //我的评论列表（GET，从token解析userId，分页）
    @GetMapping("/myList")
    public Map<String, Object> myList(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                String decoded = new String(java.util.Base64.getDecoder().decode(token));
                userId = Long.valueOf(decoded.split(":")[0]);
            } catch (Exception ignored) {
            }
        }
        Map<String, Object> result = new HashMap<>();
        if (userId == null) {
            result.put("records", Collections.emptyList());
            result.put("total", 0);
            return result;
        }
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("status", 1);
        wrapper.orderByDesc("create_time");
        Page<Comment> p = new Page<>(pageNum, pageSize);
        commentService.page(p, wrapper);
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("records", p.getRecords());
        pageData.put("total", p.getTotal());
        result.put("data", pageData);
        return result;
    }

    //更新评论（PUT）
    @PutMapping("/update/{id}")
    public boolean update(@PathVariable("id") Long id, @RequestBody Map<String, Object> map) {
        Comment comment = commentService.getById(id);
        if (comment == null) return false;
        if (map.get("content") != null) {
            comment.setContent((String) map.get("content"));
        }
        return commentService.updateById(comment);
    }

    //删除评论（DELETE）
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return commentService.removeById(id);
    }

    //分页查询
    @PostMapping("/getCommentByPage")
    public PageResult<Comment> getCommentByPage(@RequestBody Map<String, Object> map) {
        Integer currentPage = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("pageSize");

        Page<Comment> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        if (map.get("targetType") != null && !map.get("targetType").toString().isEmpty()) {
            wrapper.eq("target_type", map.get("targetType"));
        }
        if (map.get("content") != null && !map.get("content").toString().isEmpty()) {
            wrapper.like("content", map.get("content"));
        }
        wrapper.orderByDesc("create_time");
        commentService.page(page, wrapper);

        PageResult<Comment> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount((int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}
