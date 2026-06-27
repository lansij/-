package com.psychology.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psychology.pojo.Article;
import com.psychology.service.ArticleService;
import com.psychology.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    //根据id修改
    @PostMapping("/updateArticle")
    public boolean updateArticle(@RequestBody Article article) {
        return articleService.updateById(article);
    }

    //根据id查询
    @PostMapping("/getArticleById/{id}")
    public Article getArticleById(@PathVariable("id") Long id) {
        return articleService.getById(id);
    }

    //添加
    @PostMapping("/addArticle")
    public boolean addArticle(@RequestBody Article article) {
        if (article.getViewCount() == null) {
            article.setViewCount(0);
        }
        if (article.getStatus() == null) {
            article.setStatus(1);
        }
        return articleService.save(article);
    }

    //批量删除
    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list) {
        return articleService.removeBatchByIds(list);
    }

    //根据id删除
    @PostMapping("/deleteArticleById/{id}")
    public boolean deleteArticleById(@PathVariable("id") Long id) {
        return articleService.removeById(id);
    }

    //文章详情（GET，递增浏览量）
    @GetMapping("/detail/{id}")
    public Article getDetail(@PathVariable("id") Long id) {
        Article article = articleService.getById(id);
        if (article != null) {
            article.setViewCount(article.getViewCount() == null ? 1 : article.getViewCount() + 1);
            articleService.updateById(article);
        }
        return article;
    }

    //文章列表（GET，分页）
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String category,
                                     @RequestParam(required = false) String keyword) {
        Page<Article> p = new Page<>(page, size);
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        if (category != null && !category.isEmpty()) {
            wrapper.eq("category", category);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        wrapper.orderByDesc("create_time");
        articleService.page(p, wrapper);
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("records", p.getRecords());
        pageData.put("total", p.getTotal());
        Map<String, Object> result = new HashMap<>();
        result.put("data", pageData);
        return result;
    }

    //添加文章（POST /add）
    @PostMapping("/add")
    public boolean add(@RequestBody Article article) {
        if (article.getViewCount() == null) article.setViewCount(0);
        if (article.getStatus() == null) article.setStatus(1);
        return articleService.save(article);
    }

    //更新文章（PUT /update）
    @PutMapping("/update")
    public boolean update(@RequestBody Article article) {
        return articleService.updateById(article);
    }

    //删除文章（DELETE /delete/{id}）
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return articleService.removeById(id);
    }

    //分页查询
    @PostMapping("/getArticleByPage")
    public PageResult<Article> getArticleByPage(@RequestBody Map<String, Object> map) {
        String keyword = (String) map.get("keyword");
        Integer currentPage = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("pageSize");

        Page<Article> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        wrapper.orderByDesc("create_time");
        articleService.page(page, wrapper);

        PageResult<Article> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount((int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}
