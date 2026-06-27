package com.psychology.feignService;

import com.psychology.pojo.Announcement;
import com.psychology.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(value = "psychology-content-service", path = "/api/content/announcement")
public interface AnnouncementFeignService {

    @PostMapping("/updateAnnouncement")
    public boolean updateAnnouncement(@RequestBody Announcement announcement);

    @PostMapping("/getAnnouncementById/{id}")
    public Announcement getAnnouncementById(@PathVariable("id") Long id);

    @PostMapping("/addAnnouncement")
    public boolean addAnnouncement(@RequestBody Announcement announcement);

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list);

    @PostMapping("/deleteAnnouncementById/{id}")
    public boolean deleteAnnouncementById(@PathVariable("id") Long id);

    @PostMapping("/getAnnouncementByPage")
    public PageResult<Announcement> getAnnouncementByPage(@RequestBody Map<String, Object> map);
}
