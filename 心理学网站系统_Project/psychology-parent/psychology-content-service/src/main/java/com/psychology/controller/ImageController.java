package com.psychology.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psychology.pojo.Image;
import com.psychology.service.ImageService;
import com.psychology.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/updateImage")
    public boolean updateImage(@RequestBody Image image) {
        return imageService.updateById(image);
    }

    @PostMapping("/getImageById/{id}")
    public Image getImageById(@PathVariable("id") Long id) {
        return imageService.getById(id);
    }

    @PostMapping("/addImage")
    public boolean addImage(@RequestBody Image image) {
        if (image.getViewCount() == null) {
            image.setViewCount(0);
        }
        if (image.getStatus() == null) {
            image.setStatus(1);
        }
        return imageService.save(image);
    }

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list) {
        return imageService.removeBatchByIds(list);
    }

    @PostMapping("/deleteImageById/{id}")
    public boolean deleteImageById(@PathVariable("id") Long id) {
        return imageService.removeById(id);
    }

    //图片列表（GET，分页）
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        Page<Image> p = new Page<>(page, size);
        QueryWrapper<Image> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        wrapper.orderByDesc("create_time");
        imageService.page(p, wrapper);
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("records", p.getRecords());
        pageData.put("total", p.getTotal());
        Map<String, Object> result = new HashMap<>();
        result.put("data", pageData);
        return result;
    }

    @PostMapping("/getImageByPage")
    public PageResult<Image> getImageByPage(@RequestBody Map<String, Object> map) {
        String keyword = (String) map.get("keyword");
        Integer currentPage = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("pageSize");

        Page<Image> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Image> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        wrapper.orderByDesc("create_time");
        imageService.page(page, wrapper);

        PageResult<Image> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount((int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}
