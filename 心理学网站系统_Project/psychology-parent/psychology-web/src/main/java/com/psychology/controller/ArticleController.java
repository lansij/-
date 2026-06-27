package com.psychology.controller;

import com.psychology.feignService.ArticleFeignService;
import com.psychology.pojo.Article;
import com.psychology.untils.R;
import com.psychology.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleFeignService articleFeignService;

    //跳转到文章列表页面
    @GetMapping("/toList")
    public String toList() {
        return "article/list";
    }

    //跳转到文章详情页面（普通用户）
    @RequestMapping("/toDetail/{id}")
    public String toDetail(@PathVariable("id") Long id) {
        return "userMenu/article/detail";
    }

    //根据id获取文章详情（AJAX）
    @PostMapping("/getArticleById/{id}")
    @ResponseBody
    public Article getArticleById(@PathVariable("id") Long id) {
        return articleFeignService.getArticleById(id);
    }

    //跳转到添加页面
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "article/add";
    }

    //跳转到编辑页面
    @RequestMapping("/toEdit/{id}")
    public String toEdit(@PathVariable("id") Long id, jakarta.servlet.http.HttpServletRequest request) {
        Article article = articleFeignService.getArticleById(id);
        request.setAttribute("article", article);
        return "article/edit";
    }

    //分页查询
    @PostMapping("/getPage")
    @ResponseBody
    public PageResult<Article> getPage(@RequestParam(defaultValue = "") String keyword,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword.isEmpty() ? null : keyword);
        map.put("page", page);
        map.put("pageSize", limit);
        return articleFeignService.getArticleByPage(map);
    }

    //执行添加
    @PostMapping("/doAdd")
    @ResponseBody
    public R doAdd(Article article) {
        boolean flag = articleFeignService.addArticle(article);
        return flag ? R.right() : R.error();
    }

    //执行修改
    @PostMapping("/doEdit")
    @ResponseBody
    public R doEdit(Article article) {
        boolean flag = articleFeignService.updateArticle(article);
        return flag ? R.right() : R.error();
    }

    //单个删除
    @PostMapping("/doDel/{id}")
    @ResponseBody
    public R doDel(@PathVariable("id") Long id) {
        boolean flag = articleFeignService.deleteArticleById(id);
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
        boolean flag = articleFeignService.deleteByIds(list);
        return flag ? R.right() : R.error();
    }
}
