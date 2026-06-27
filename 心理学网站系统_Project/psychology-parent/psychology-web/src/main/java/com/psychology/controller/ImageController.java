package com.psychology.controller;

import com.psychology.feignService.ImageFeignService;
import com.psychology.pojo.Image;
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
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageFeignService imageFeignService;

    @GetMapping("/toList")
    public String toList() {
        return "image/list";
    }

    //跳转到图片详情页面（普通用户）
    @RequestMapping("/toDetail/{id}")
    public String toDetail(@PathVariable("id") Long id) {
        return "userMenu/image/detail";
    }

    //根据id获取图片详情（AJAX）
    @PostMapping("/getImageById/{id}")
    @ResponseBody
    public Image getImageById(@PathVariable("id") Long id) {
        return imageFeignService.getImageById(id);
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "image/add";
    }

    @RequestMapping("/toEdit/{id}")
    public String toEdit(@PathVariable("id") Long id, jakarta.servlet.http.HttpServletRequest request) {
        Image image = imageFeignService.getImageById(id);
        request.setAttribute("image", image);
        return "image/edit";
    }

    @PostMapping("/getPage")
    @ResponseBody
    public PageResult<Image> getPage(@RequestParam(defaultValue = "") String keyword,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword.isEmpty() ? null : keyword);
        map.put("page", page);
        map.put("pageSize", limit);
        return imageFeignService.getImageByPage(map);
    }

    @PostMapping("/doAdd")
    @ResponseBody
    public R doAdd(Image image) {
        boolean flag = imageFeignService.addImage(image);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/doEdit")
    @ResponseBody
    public R doEdit(Image image) {
        boolean flag = imageFeignService.updateImage(image);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/doDel/{id}")
    @ResponseBody
    public R doDel(@PathVariable("id") Long id) {
        boolean flag = imageFeignService.deleteImageById(id);
        return flag ? R.right() : R.error();
    }

    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestParam(value = "ids[]") Long ids[]) {
        List<Long> list = new ArrayList<>();
        for (Long id : ids) {
            list.add(id);
        }
        boolean flag = imageFeignService.deleteByIds(list);
        return flag ? R.right() : R.error();
    }
}
