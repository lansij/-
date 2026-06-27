package com.psychology.feignService;

import com.psychology.pojo.Book;
import com.psychology.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(value = "psychology-content-service", path = "/api/content/book")
public interface BookFeignService {

    @PostMapping("/updateBook")
    public boolean updateBook(@RequestBody Book book);

    @PostMapping("/getBookById/{id}")
    public Book getBookById(@PathVariable("id") Long id);

    @PostMapping("/addBook")
    public boolean addBook(@RequestBody Book book);

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list);

    @PostMapping("/deleteBookById/{id}")
    public boolean deleteBookById(@PathVariable("id") Long id);

    @PostMapping("/getBookByPage")
    public PageResult<Book> getBookByPage(@RequestBody Map<String, Object> map);
}
