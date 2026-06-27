package com.psychology.feignService;

import com.psychology.pojo.Image;
import com.psychology.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(value = "psychology-content-service", path = "/api/content/image")
public interface ImageFeignService {

    @PostMapping("/updateImage")
    public boolean updateImage(@RequestBody Image image);

    @PostMapping("/getImageById/{id}")
    public Image getImageById(@PathVariable("id") Long id);

    @PostMapping("/addImage")
    public boolean addImage(@RequestBody Image image);

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list);

    @PostMapping("/deleteImageById/{id}")
    public boolean deleteImageById(@PathVariable("id") Long id);

    @PostMapping("/getImageByPage")
    public PageResult<Image> getImageByPage(@RequestBody Map<String, Object> map);
}
