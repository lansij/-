package com.psychology.controller;

import com.psychology.pojo.User;
import com.psychology.service.UserService;
import com.psychology.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    //用户登录
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        User user2 = userService.login(user);
        if (user2 != null) {
            user2.setPassword(null); // 不返回密码
        }
        return user2;
    }

    //用户注册
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        boolean ok = userService.register(user);
        Map<String, Object> result = new HashMap<>();
        if (ok) {
            result.put("code", 200);
            result.put("msg", "注册成功");
        } else {
            result.put("code", 500);
            result.put("msg", "用户名已存在");
        }
        return result;
    }

    //添加用户（管理员）
    @PostMapping("/add")
    public boolean add(@RequestBody User user) {
        return userService.register(user);
    }

    //管理员更新用户
    @PutMapping("/adminUpdate")
    public boolean adminUpdate(@RequestBody User user) {
        return userService.updateById(user);
    }

    //删除用户
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return userService.removeById(id);
    }

    //获取当前用户信息（从token解析userId）
    @GetMapping("/info")
    public Map<String, Object> info(@RequestHeader(value = "Authorization", required = false) String authorization) {
        Map<String, Object> result = new HashMap<>();
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            result.put("code", 401);
            result.put("msg", "未登录");
            return result;
        }
        try {
            String token = authorization.substring(7);
            String decoded = new String(java.util.Base64.getDecoder().decode(token));
            Long userId = Long.valueOf(decoded.split(":")[0]);
            User user = userService.getById(userId);
            if (user != null) {
                user.setPassword(null);
                result.put("code", 200);
                result.put("data", user);
            } else {
                result.put("code", 404);
                result.put("msg", "用户不存在");
            }
        } catch (Exception e) {
            result.put("code", 401);
            result.put("msg", "token无效");
        }
        return result;
    }

    //用户列表（分页，管理员用）
    @GetMapping("/list")
    public Map<String, Object> userList(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) String keyword) {
        Page<User> p = new Page<>(page, size);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("username", keyword).or().like("nickname", keyword);
        }
        wrapper.orderByDesc("create_time");
        userService.page(p, wrapper);
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("records", p.getRecords());
        pageData.put("total", p.getTotal());
        Map<String, Object> result = new HashMap<>();
        result.put("data", pageData);
        return result;
    }

    //根据id查询用户信息
    @PostMapping("/getUserById/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    //修改用户信息
    @PostMapping("/updateUser")
    public boolean updateUser(@RequestBody User user) {
        return userService.updateById(user);
    }

    //更新当前用户信息（从token解析userId）
    @PutMapping("/update")
    public Map<String, Object> updateCurrentUser(@RequestBody Map<String, Object> map,
                                                  @RequestHeader(value = "Authorization", required = false) String authorization) {
        Map<String, Object> result = new HashMap<>();
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                String decoded = new String(java.util.Base64.getDecoder().decode(token));
                userId = Long.valueOf(decoded.split(":")[0]);
            } catch (Exception ignored) {
            }
        }
        if (userId == null) {
            result.put("code", 401);
            result.put("msg", "未登录");
            return result;
        }
        User user = userService.getById(userId);
        if (user == null) {
            result.put("code", 404);
            result.put("msg", "用户不存在");
            return result;
        }
        if (map.get("nickname") != null) user.setNickname((String) map.get("nickname"));
        if (map.get("email") != null) user.setEmail((String) map.get("email"));
        if (map.get("phone") != null) user.setPhone((String) map.get("phone"));
        boolean ok = userService.updateById(user);
        result.put("code", ok ? 200 : 500);
        result.put("msg", ok ? "更新成功" : "更新失败");
        return result;
    }

    //分页查询用户
    @PostMapping("/getUserByPage")
    public PageResult<User> getUserByPage(@RequestBody Map<String, Object> map) {
        String keyword = (String) map.get("keyword");
        Integer currentPage = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("pageSize");

        Page<User> page = new Page<>(currentPage, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("username", keyword).or().like("nickname", keyword);
        }
        wrapper.orderByDesc("create_time");
        userService.page(page, wrapper);

        PageResult<User> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount((int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }

    //根据id删除用户
    @PostMapping("/deleteUserById/{id}")
    public boolean deleteUserById(@PathVariable("id") Long id) {
        return userService.removeById(id);
    }

    //批量删除
    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list) {
        return userService.removeBatchByIds(list);
    }
}
