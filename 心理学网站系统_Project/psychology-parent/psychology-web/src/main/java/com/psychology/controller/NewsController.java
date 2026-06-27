package com.psychology.controller;

import com.psychology.feignService.NewsFeignService;
import com.psychology.pojo.News;
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
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsFeignService newsFeignService;

    @GetMapping("/toList")
    public String toList() {
        return "news/list";
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "news/add";
    }

    @RequestMapping("/toEdit/{id}")
    public String toEdit(@PathVariable("id") Long id, jakarta.servlet.http.HttpServletRequest request) {
        News news = newsFeignService.getNewsById(id);
        request.setAttribute("news", news);
        return "news/edit";
    }

    @PostMapping("/getPage")
    @ResponseBody
    public PageResult<News> getPage(@RequestParam(defaultValue = "") String keyword,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword.isEmpty() ? null : keyword);
        map.put("page", page);
        map.put("pageSize", limit);
        return newsFeignService.getNewsByPage(map);
    }

    @PostMapping("/doAdd")
    @ResponseBody
    public R doAdd(News news) {
        boolean flag = newsFeignService.addNews(news);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/doEdit")
    @ResponseBody
    public R doEdit(News news) {
        boolean flag = newsFeignService.updateNews(news);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/doDel/{id}")
    @ResponseBody
    public R doDel(@PathVariable("id") Long id) {
        boolean flag = newsFeignService.deleteNewsById(id);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestParam(value = "ids[]") Long ids[]) {
        List<Long> list = new ArrayList<>();
        for (Long id : ids) {
            list.add(id);
        }
        boolean flag = newsFeignService.deleteByIds(list);
        return flag ? R.right() : R.error();
    }
}
