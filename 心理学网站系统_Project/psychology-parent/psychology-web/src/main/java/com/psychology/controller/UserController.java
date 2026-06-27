package com.psychology.controller;

import com.psychology.feignService.UserFeignService;
import com.psychology.pojo.User;
import com.psychology.untils.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserFeignService userFeignService;

    //退出登录
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        System.out.println("--------logout--------");
        session.invalidate();
        return "redirect:/user/toLogin";
    }

    //处理用户登录
    @RequestMapping("/login")
    @ResponseBody
    public R login(User user, HttpSession session) {
        System.out.println("----------login---------");
        System.out.println("user:" + user);

        User user2 = userFeignService.login(user);
        System.out.println("user2:" + user2);
        if (user2 != null) {
            session.setAttribute("user", user2);
            R r = R.right();
            r.put("role", user2.getRole());
            return r;
        } else {
            return R.error();
        }
    }

    //跳转到登录页面
    @RequestMapping("/toLogin")
    public String toLogin(HttpServletRequest request) {
        System.out.println("-----------toLogin---------");

        //创建角色列表
        List<Map<String, Object>> roleList = new ArrayList<>();
        Map<String, Object> adminRole = new HashMap<>();
        adminRole.put("roleName", "管理员");
        adminRole.put("roleValue", "ADMIN");
        roleList.add(adminRole);

        Map<String, Object> userRole = new HashMap<>();
        userRole.put("roleName", "普通用户");
        userRole.put("roleValue", "USER");
        roleList.add(userRole);

        request.setAttribute("roleList", roleList);
        return "user/login";
    }

    //跳转到注册页面
    @RequestMapping("/toRegister")
    public String toRegister() {
        return "user/register";
    }

    //处理注册
    @RequestMapping("/doRegister")
    @ResponseBody
    public R doRegister(User user) {
        System.out.println("----------doRegister---------");
        boolean flag = userFeignService.register(user);
        if (flag) {
            return R.right();
        } else {
            return R.error("用户名已存在");
        }
    }

    //跳转到用户中心
    @RequestMapping("/toCenter")
    public String toCenter() {
        return "user/center";
    }

    //修改个人信息
    @RequestMapping("/doUpdateProfile")
    @ResponseBody
    public R doUpdateProfile(User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        user.setId(currentUser.getId());
        boolean flag = userFeignService.updateUser(user);
        if (flag) {
            //更新session
            User updatedUser = userFeignService.getUserById(currentUser.getId());
            session.setAttribute("user", updatedUser);
            return R.right();
        } else {
            return R.error();
        }
    }

    //修改密码
    @RequestMapping("/doUpdatePassword")
    @ResponseBody
    public R doUpdatePassword(String oldPassword, String newPassword, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        //用原密码登录验证
        User verifyUser = new User();
        verifyUser.setUsername(currentUser.getUsername());
        verifyUser.setPassword(oldPassword);
        User checked = userFeignService.login(verifyUser);
        if (checked == null) {
            return R.error("原密码错误");
        }
        //更新密码
        User updateUser = new User();
        updateUser.setId(currentUser.getId());
        updateUser.setPassword(newPassword);
        boolean flag = userFeignService.updateUser(updateUser);
        return flag ? R.right() : R.error("修改失败");
    }
}
