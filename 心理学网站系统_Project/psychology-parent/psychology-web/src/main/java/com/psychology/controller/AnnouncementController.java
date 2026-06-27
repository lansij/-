package com.psychology.controller;

import com.psychology.feignService.AnnouncementFeignService;
import com.psychology.pojo.Announcement;
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
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementFeignService announcementFeignService;

    @GetMapping("/toList")
    public String toList() {
        return "announcement/list";
    }

    @GetMapping("/toDetail/{id}")
    public String toDetail(@PathVariable("id") Long id) {
        return "userMenu/announcement/detail";
    }

    @PostMapping("/getAnnouncementById/{id}")
    @ResponseBody
    public Announcement getAnnouncementById(@PathVariable("id") Long id) {
        return announcementFeignService.getAnnouncementById(id);
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "announcement/add";
    }

    @RequestMapping("/toEdit/{id}")
    public String toEdit(@PathVariable("id") Long id, jakarta.servlet.http.HttpServletRequest request) {
        Announcement announcement = announcementFeignService.getAnnouncementById(id);
        request.setAttribute("announcement", announcement);
        return "announcement/edit";
    }

    @PostMapping("/getPage")
    @ResponseBody
    public PageResult<Announcement> getPage(@RequestParam(defaultValue = "") String keyword,
                                            @RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword.isEmpty() ? null : keyword);
        map.put("page", page);
        map.put("pageSize", limit);
        return announcementFeignService.getAnnouncementByPage(map);
    }

    @PostMapping("/doAdd")
    @ResponseBody
    public R doAdd(Announcement announcement) {
        boolean flag = announcementFeignService.addAnnouncement(announcement);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/doEdit")
    @ResponseBody
    public R doEdit(Announcement announcement) {
        boolean flag = announcementFeignService.updateAnnouncement(announcement);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/doDel/{id}")
    @ResponseBody
    public R doDel(@PathVariable("id") Long id) {
        boolean flag = announcementFeignService.deleteAnnouncementById(id);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestParam(value = "ids[]") Long ids[]) {
        List<Long> list = new ArrayList<>();
        for (Long id : ids) {
            list.add(id);
        }
        boolean flag = announcementFeignService.deleteByIds(list);
        return flag ? R.right() : R.error();
    }
}
