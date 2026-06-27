package com.psychology.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psychology.pojo.Favorite;
import com.psychology.service.FavoriteService;
import com.psychology.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    //添加收藏
    @PostMapping("/addFavorite")
    public boolean addFavorite(@RequestBody Favorite favorite) {
        //检查是否已收藏
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", favorite.getUserId());
        wrapper.eq("target_type", favorite.getTargetType());
        wrapper.eq("target_id", favorite.getTargetId());
        long count = favoriteService.count(wrapper);
        if (count > 0) {
            return false;
        }
        return favoriteService.save(favorite);
    }

    //取消收藏
    @PostMapping("/removeFavorite")
    public boolean removeFavorite(@RequestBody Favorite favorite) {
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", favorite.getUserId());
        wrapper.eq("target_type", favorite.getTargetType());
        wrapper.eq("target_id", favorite.getTargetId());
        return favoriteService.remove(wrapper);
    }

    //检查是否已收藏
    @PostMapping("/checkFavorite")
    public boolean checkFavorite(@RequestBody Map<String, Object> map) {
        Long userId = Long.valueOf(map.get("userId").toString());
        String targetType = (String) map.get("targetType");
        Long targetId = Long.valueOf(map.get("targetId").toString());
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("target_type", targetType);
        wrapper.eq("target_id", targetId);
        return favoriteService.count(wrapper) > 0;
    }

    //根据用户id和类型查询收藏
    @PostMapping("/getFavoriteByUser")
    public List<Favorite> getFavoriteByUser(@RequestBody Map<String, Object> map) {
        Long userId = Long.valueOf(map.get("userId").toString());
        String targetType = (String) map.get("targetType");
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("target_type", targetType);
        wrapper.orderByDesc("create_time");
        return favoriteService.list(wrapper);
    }

    //分页查询
    @PostMapping("/getFavoriteByPage")
    public PageResult<Favorite> getFavoriteByPage(@RequestBody Map<String, Object> map) {
        Integer currentPage = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("pageSize");
        Long userId = map.get("userId") != null ? Long.valueOf(map.get("userId").toString()) : null;

        Page<Favorite> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        wrapper.orderByDesc("create_time");
        favoriteService.page(page, wrapper);

        PageResult<Favorite> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount((int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }

    //切换收藏状态（POST，从token解析userId）
    @PostMapping("/toggle")
    public Map<String, Object> toggle(@RequestBody Map<String, Object> map,
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
        if (userId == null && map.get("userId") != null) {
            userId = Long.valueOf(map.get("userId").toString());
        }
        if (userId == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("code", 401);
            err.put("msg", "未登录");
            return err;
        }
        String targetType = (String) map.get("targetType");
        Long targetId = Long.valueOf(map.get("targetId").toString());

        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("target_type", targetType);
        wrapper.eq("target_id", targetId);
        long count = favoriteService.count(wrapper);

        Map<String, Object> result = new HashMap<>();
        if (count > 0) {
            favoriteService.remove(wrapper);
            result.put("favorited", false);
        } else {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setTargetType(targetType);
            favorite.setTargetId(targetId);
            favoriteService.save(favorite);
            result.put("favorited", true);
        }
        return result;
    }

    //我的收藏列表（GET，从token解析userId）
    @GetMapping("/myList")
    public Map<String, Object> myList(@RequestParam(required = false) String targetType,
                                       @RequestParam(defaultValue = "1") Integer pageNum,
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
        Map<String, Object> pageData = new HashMap<>();
        if (userId == null) {
            pageData.put("records", java.util.Collections.emptyList());
            pageData.put("total", 0);
            result.put("data", pageData);
            return result;
        }
        QueryWrapper<Favorite> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (targetType != null && !targetType.isEmpty()) {
            wrapper.eq("target_type", targetType);
        }
        wrapper.orderByDesc("create_time");
        Page<Favorite> p = new Page<>(pageNum, pageSize);
        favoriteService.page(p, wrapper);
        pageData.put("records", p.getRecords());
        pageData.put("total", p.getTotal());
        result.put("data", pageData);
        return result;
    }

    //取消收藏（DELETE）
    @DeleteMapping("/cancel/{id}")
    public boolean cancel(@PathVariable("id") Long id) {
        return favoriteService.removeById(id);
    }

    //批量删除
    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list) {
        return favoriteService.removeBatchByIds(list);
    }
}
