package com.psychology.controller;

import com.psychology.utils.SysMenu;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class MainController {

    //根路径重定向到用户首页
    @RequestMapping("/")
    public String root() {
        return "redirect:/toUserIndex";
    }

    //跳转到管理员登录成功的后台首页
    @RequestMapping("/toIndex")
    public String toContent(HttpServletRequest request) {
        request.setAttribute("userMenuList", getSysMenu());
        return "index";
    }

    //跳转到普通用户登录成功的后台首页
    @RequestMapping("/toUserIndex")
    public String toUserContent(HttpServletRequest request) {
        request.setAttribute("userMenuList", getUserMenu());
        return "userMenu/userContent";
    }

    //返回欢迎页面
    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    //管理员的菜单
    private List<SysMenu> getSysMenu() {
        List<SysMenu> ml = new ArrayList<SysMenu>();

        SysMenu m1 = new SysMenu("system", "系统管理", "layui-icon layui-icon-util", false, null);
        SysMenu m1_1 = new SysMenu("system", "用户管理", "layui-icon layui-icon-group", true, "/usermanage/toList");
        SysMenu m1_2 = new SysMenu("system", "新闻管理", "layui-icon layui-icon-read", true, "/news/toList");
        SysMenu m1_3 = new SysMenu("system", "文章管理", "layui-icon layui-icon-edit", true, "/article/toList");
        SysMenu m1_4 = new SysMenu("system", "书籍管理", "layui-icon layui-icon-note", true, "/book/toList");
        SysMenu m1_5 = new SysMenu("system", "图片管理", "layui-icon layui-icon-picture", true, "/image/toList");
        SysMenu m1_6 = new SysMenu("system", "公告管理", "layui-icon layui-icon-notice", true, "/announcement/toList");
        SysMenu m1_7 = new SysMenu("system", "评论管理", "layui-icon layui-icon-reply-fill", true, "/comment/toList");
        SysMenu m1_8 = new SysMenu("system", "收藏管理", "layui-icon layui-icon-star", true, "/favorite/toList");

        m1.getChildMenuList().add(m1_1);
        m1.getChildMenuList().add(m1_2);
        m1.getChildMenuList().add(m1_3);
        m1.getChildMenuList().add(m1_4);
        m1.getChildMenuList().add(m1_5);
        m1.getChildMenuList().add(m1_6);
        m1.getChildMenuList().add(m1_7);
        m1.getChildMenuList().add(m1_8);
        ml.add(m1);

        return ml;
    }

    //用户的菜单
    private List<SysMenu> getUserMenu() {
        List<SysMenu> ml = new ArrayList<SysMenu>();

        SysMenu m1 = new SysMenu("system", "个人中心", "layui-icon layui-icon-util", false, null);
        SysMenu m1_1 = new SysMenu("system", "个人信息", "layui-icon layui-icon-set", true, "/user/toCenter");
        SysMenu m1_2 = new SysMenu("system", "我的评论", "layui-icon layui-icon-reply-fill", true, "/comment/toMyList");
        SysMenu m1_3 = new SysMenu("system", "文章收藏", "layui-icon layui-icon-edit", true, "/favorite/toMyArticleList");
        SysMenu m1_4 = new SysMenu("system", "书籍收藏", "layui-icon layui-icon-note", true, "/favorite/toMyBookList");

        m1.getChildMenuList().add(m1_1);
        m1.getChildMenuList().add(m1_2);
        m1.getChildMenuList().add(m1_3);
        m1.getChildMenuList().add(m1_4);
        ml.add(m1);

        return ml;
    }
}
