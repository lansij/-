package com.psychology.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psychology.pojo.News;
import com.psychology.service.NewsService;
import com.psychology.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PostMapping("/updateNews")
    public boolean updateNews(@RequestBody News news) {
        return newsService.updateById(news);
    }

    @PostMapping("/getNewsById/{id}")
    public News getNewsById(@PathVariable("id") Long id) {
        return newsService.getById(id);
    }

    @PostMapping("/addNews")
    public boolean addNews(@RequestBody News news) {
        if (news.getViewCount() == null) {
            news.setViewCount(0);
        }
        if (news.getStatus() == null) {
            news.setStatus(1);
        }
        return newsService.save(news);
    }

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list) {
        return newsService.removeBatchByIds(list);
    }

    @PostMapping("/deleteNewsById/{id}")
    public boolean deleteNewsById(@PathVariable("id") Long id) {
        return newsService.removeById(id);
    }

    //新闻详情（GET，递增浏览量）
    @GetMapping("/detail/{id}")
    public News getDetail(@PathVariable("id") Long id) {
        News news = newsService.getById(id);
        if (news != null) {
            news.setViewCount(news.getViewCount() == null ? 1 : news.getViewCount() + 1);
            newsService.updateById(news);
        }
        return news;
    }

    //新闻列表（GET，分页）
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String keyword) {
        Page<News> p = new Page<>(page, size);
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        wrapper.orderByDesc("create_time");
        newsService.page(p, wrapper);
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("records", p.getRecords());
        pageData.put("total", p.getTotal());
        Map<String, Object> result = new HashMap<>();
        result.put("data", pageData);
        return result;
    }

    @PostMapping("/add")
    public boolean add(@RequestBody News news) {
        if (news.getViewCount() == null) news.setViewCount(0);
        if (news.getStatus() == null) news.setStatus(1);
        return newsService.save(news);
    }

    @PutMapping("/update")
    public boolean update(@RequestBody News news) {
        return newsService.updateById(news);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return newsService.removeById(id);
    }

    @PostMapping("/getNewsByPage")
    public PageResult<News> getNewsByPage(@RequestBody Map<String, Object> map) {
        String keyword = (String) map.get("keyword");
        Integer currentPage = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("pageSize");

        Page<News> page = new Page<>(currentPage, pageSize);
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        wrapper.orderByDesc("create_time");
        newsService.page(page, wrapper);

        PageResult<News> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount((int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}
