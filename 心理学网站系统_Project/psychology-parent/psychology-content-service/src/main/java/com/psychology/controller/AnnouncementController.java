package com.psychology.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.psychology.pojo.Announcement;
import com.psychology.service.AnnouncementService;
import com.psychology.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @PostMapping("/updateAnnouncement")
    public boolean updateAnnouncement(@RequestBody Announcement announcement) {
        return announcementService.updateById(announcement);
    }

    @PostMapping("/getAnnouncementById/{id}")
    public Announcement getAnnouncementById(@PathVariable("id") Long id) {
        return announcementService.getById(id);
    }

    @PostMapping("/addAnnouncement")
    public boolean addAnnouncement(@RequestBody Announcement announcement) {
        if (announcement.getViewCount() == null) {
            announcement.setViewCount(0);
        }
        if (announcement.getStatus() == null) {
            announcement.setStatus(1);
        }
        return announcementService.save(announcement);
    }

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list) {
        return announcementService.removeBatchByIds(list);
    }

    @PostMapping("/deleteAnnouncementById/{id}")
    public boolean deleteAnnouncementById(@PathVariable("id") Long id) {
        return announcementService.removeById(id);
    }

    //公告列表（GET，分页）
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size,
                                     @RequestParam(required = false) String keyword) {
        Page<Announcement> p = new Page<>(page, size);
        QueryWrapper<Announcement> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        wrapper.orderByDesc("create_time");
        announcementService.page(p, wrapper);
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("records", p.getRecords());
        pageData.put("total", p.getTotal());
        Map<String, Object> result = new HashMap<>();
        result.put("data", pageData);
        return result;
    }

    @PostMapping("/add")
    public boolean add(@RequestBody Announcement announcement) {
        if (announcement.getViewCount() == null) announcement.setViewCount(0);
        if (announcement.getStatus() == null) announcement.setStatus(1);
        return announcementService.save(announcement);
    }

    @PutMapping("/update")
    public boolean update(@RequestBody Announcement announcement) {
        return announcementService.updateById(announcement);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return announcementService.removeById(id);
    }

    @PostMapping("/getAnnouncementByPage")
    public PageResult<Announcement> getAnnouncementByPage(@RequestBody Map<String, Object> map) {
        String keyword = (String) map.get("keyword");
        Integer currentPage = (Integer) map.get("page");
        Integer pageSize = (Integer) map.get("pageSize");

        Page<Announcement> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Announcement> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        wrapper.orderByDesc("create_time");
        announcementService.page(page, wrapper);

        PageResult<Announcement> result = new PageResult<>();
        result.setCode(0);
        result.setMsg("查询成功");
        result.setCount((int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }
}
