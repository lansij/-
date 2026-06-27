package com.psychology.feignService;

import com.psychology.pojo.News;
import com.psychology.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(value = "psychology-content-service", path = "/api/content/news")
public interface NewsFeignService {

    @PostMapping("/updateNews")
    public boolean updateNews(@RequestBody News news);

    @PostMapping("/getNewsById/{id}")
    public News getNewsById(@PathVariable("id") Long id);

    @PostMapping("/addNews")
    public boolean addNews(@RequestBody News news);

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list);

    @PostMapping("/deleteNewsById/{id}")
    public boolean deleteNewsById(@PathVariable("id") Long id);

    @PostMapping("/getNewsByPage")
    public PageResult<News> getNewsByPage(@RequestBody Map<String, Object> map);
}
