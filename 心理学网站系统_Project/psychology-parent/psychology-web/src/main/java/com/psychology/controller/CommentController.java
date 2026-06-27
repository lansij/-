package com.psychology.controller;

import com.psychology.feignService.CommentFeignService;
import com.psychology.feignService.UserFeignService;
import com.psychology.pojo.Comment;
import com.psychology.pojo.User;
import com.psychology.untils.R;
import com.psychology.utils.PageResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentFeignService commentFeignService;

    @Autowired
    private UserFeignService userFeignService;

    //管理员评论列表
    @GetMapping("/toList")
    public String toList() {
        return "comment/list";
    }

    //用户我的评论
    @RequestMapping("/toMyList")
    public String toMyList() {
        return "comment/myList";
    }

    //分页查询（含用户昵称）
    @PostMapping("/getPage")
    @ResponseBody
    public PageResult<Map<String, Object>> getPage(@RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer limit,
                                                    @RequestParam(required = false) String targetType,
                                                    @RequestParam(required = false) String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("pageSize", limit);
        if (targetType != null && !targetType.isEmpty()) {
            map.put("targetType", targetType);
        }
        if (content != null && !content.isEmpty()) {
            map.put("content", content);
        }
        PageResult<Comment> commentPage = commentFeignService.getCommentByPage(map);

        List<Map<String, Object>> list = new ArrayList<>();
        for (Comment c : commentPage.getData()) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getId());
            item.put("userId", c.getUserId());
            item.put("targetType", c.getTargetType());
            item.put("targetId", c.getTargetId());
            item.put("content", c.getContent());
            item.put("createTime", c.getCreateTime());
            User user = userFeignService.getUserById(c.getUserId());
            item.put("nickname", user != null && user.getNickname() != null ? user.getNickname() : "未知用户");
            list.add(item);
        }

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount(commentPage.getCount());
        result.setData(list);
        return result;
    }

    //查询用户自己的评论
    @PostMapping("/getMyComments")
    @ResponseBody
    public PageResult<Comment> getMyComments(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Comment> list = commentFeignService.getCommentByUserId(user.getId());
        PageResult<Comment> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount(list.size());
        result.setData(list);
        return result;
    }

    //修改评论
    @PostMapping("/doEdit")
    @ResponseBody
    public R doEdit(@RequestParam Long id, @RequestParam String content) {
        Comment comment = commentFeignService.getCommentById(id);
        if (comment == null) return R.error("评论不存在");
        comment.setContent(content);
        boolean flag = commentFeignService.updateComment(comment);
        return flag ? R.right() : R.error();
    }

    //删除评论
    @PostMapping("/doDel/{id}")
    @ResponseBody
    public R doDel(@PathVariable("id") Long id) {
        boolean flag = commentFeignService.deleteCommentById(id);
        return flag ? R.right() : R.error();
    }

    //批量删除
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestParam(value = "ids[]") Long ids[]) {
        List<Long> list = new ArrayList<>();
        for (Long id : ids) {
            list.add(id);
        }
        boolean flag = commentFeignService.deleteByIds(list);
        return flag ? R.right() : R.error();
    }

    //根据目标类型和目标ID获取评论列表
    @PostMapping("/getByTarget")
    @ResponseBody
    public List<Comment> getByTarget(@RequestParam String targetType, @RequestParam Long targetId) {
        Map<String, Object> map = new HashMap<>();
        map.put("targetType", targetType);
        map.put("targetId", targetId);
        return commentFeignService.getCommentByTarget(map);
    }

    //添加评论
    @PostMapping("/add")
    @ResponseBody
    public R add(@RequestParam String targetType, @RequestParam Long targetId,
                 @RequestParam String content, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return R.error("请先登录");
        }
        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setTargetType(targetType);
        comment.setTargetId(targetId);
        comment.setContent(content);
        comment.setStatus(1);
        boolean flag = commentFeignService.addComment(comment);
        return flag ? R.right() : R.error();
    }
}
