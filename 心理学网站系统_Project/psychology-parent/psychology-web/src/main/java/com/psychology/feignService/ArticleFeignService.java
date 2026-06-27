package com.psychology.feignService;

import com.psychology.pojo.Article;
import com.psychology.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(value = "psychology-content-service", path = "/api/content/article")
public interface ArticleFeignService {

    @PostMapping("/updateArticle")
    public boolean updateArticle(@RequestBody Article article);

    @PostMapping("/getArticleById/{id}")
    public Article getArticleById(@PathVariable("id") Long id);

    @PostMapping("/addArticle")
    public boolean addArticle(@RequestBody Article article);

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list);

    @PostMapping("/deleteArticleById/{id}")
    public boolean deleteArticleById(@PathVariable("id") Long id);

    @PostMapping("/getArticleByPage")
    public PageResult<Article> getArticleByPage(@RequestBody Map<String, Object> map);
}
