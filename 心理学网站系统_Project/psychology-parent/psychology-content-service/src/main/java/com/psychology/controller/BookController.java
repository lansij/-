package com.psychology.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psychology.pojo.Book;
import com.psychology.service.BookService;
import com.psychology.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/updateBook")
    public boolean updateBook(@RequestBody Book book) {
        return bookService.updateById(book);
    }

    @PostMapping("/getBookById/{id}")
    public Book getBookById(@PathVariable("id") Long id) {
        return bookService.getById(id);
    }

    @PostMapping("/addBook")
    public boolean addBook(@RequestBody Book book) {
        if (book.getViewCount() == null) {
            book.setViewCount(0);
        }
        if (book.getStatus() == null) {
            book.setStatus(1);
        }
        return bookService.save(book);
    }

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list) {
        return bookService.removeBatchByIds(list);
    }

    @PostMapping("/deleteBookById/{id}")
    public boolean deleteBookById(@PathVariable("id") Long id) {
        return bookService.removeById(id);
    }

    //书籍详情（GET，递增浏览量）
    @GetMapping("/detail/{id}")
    public Book getDetail(@PathVariable("id") Long id) {
        Book book = bookService.getById(id);
        if (book != null) {
            book.setViewCount(book.getViewCount() == null ? 1 : book.getViewCount() + 1);
            bookService.updateById(book);
        }
        return book;
    }

    //书籍列表（GET，分页）
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String keyword) {
        Page<Book> p = new Page<>(page, size);
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        wrapper.orderByDesc("create_time");
        bookService.page(p, wrapper);
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("records", p.getRecords());
        pageData.put("total", p.getTotal());
        Map<String, Object> result = new HashMap<>();
        result.put("data", pageData);
        return result;
    }

    @PostMapping("/add")
    public boolean add(@RequestBody Book book) {
        if (book.getViewCount() == null) book.setViewCount(0);
        if (book.getStatus() == null) book.setStatus(1);
        return bookService.save(book);
    }

    @PutMapping("/update")
    public boolean update(@RequestBody Book book) {
        return bookService.updateById(book);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return bookService.removeById(id);
    }

    @PostMapping("/getBookByPage")
    public PageResult<Book> getBookByPage(@RequestBody Map<String, Object> map) {
        String keyword = (String) map.get("keyword");
        Integer currentPage = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("pageSize");

        Page<Book> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        wrapper.orderByDesc("create_time");
        bookService.page(page, wrapper);

        PageResult<Book> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount((int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}
