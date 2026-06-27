package com.psychology.controller;

import com.psychology.feignService.BookFeignService;
import com.psychology.pojo.Book;
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
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookFeignService bookFeignService;

    @GetMapping("/toList")
    public String toList() {
        return "book/list";
    }

    //跳转到书籍详情页面（普通用户）
    @RequestMapping("/toDetail/{id}")
    public String toDetail(@PathVariable("id") Long id) {
        return "userMenu/book/detail";
    }

    //根据id获取书籍详情（AJAX）
    @PostMapping("/getBookById/{id}")
    @ResponseBody
    public Book getBookById(@PathVariable("id") Long id) {
        return bookFeignService.getBookById(id);
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "book/add";
    }

    @RequestMapping("/toEdit/{id}")
    public String toEdit(@PathVariable("id") Long id, jakarta.servlet.http.HttpServletRequest request) {
        Book book = bookFeignService.getBookById(id);
        request.setAttribute("book", book);
        return "book/edit";
    }

    @PostMapping("/getPage")
    @ResponseBody
    public PageResult<Book> getPage(@RequestParam(defaultValue = "") String keyword,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword.isEmpty() ? null : keyword);
        map.put("page", page);
        map.put("pageSize", limit);
        return bookFeignService.getBookByPage(map);
    }

    @PostMapping("/doAdd")
    @ResponseBody
    public R doAdd(Book book) {
        boolean flag = bookFeignService.addBook(book);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/doEdit")
    @ResponseBody
    public R doEdit(Book book) {
        boolean flag = bookFeignService.updateBook(book);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/doDel/{id}")
    @ResponseBody
    public R doDel(@PathVariable("id") Long id) {
        boolean flag = bookFeignService.deleteBookById(id);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestParam(value = "ids[]") Long ids[]) {
        List<Long> list = new ArrayList<>();
        for (Long id : ids) {
            list.add(id);
        }
        boolean flag = bookFeignService.deleteByIds(list);
        return flag ? R.right() : R.error();
    }
}
