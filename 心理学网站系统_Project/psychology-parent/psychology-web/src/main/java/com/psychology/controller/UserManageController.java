package com.psychology.controller;

import com.psychology.feignService.UserFeignService;
import com.psychology.pojo.User;
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
@RequestMapping("/usermanage")
public class UserManageController {

    @Autowired
    private UserFeignService userFeignService;

    //跳转到用户管理列表页面
    @GetMapping("/toList")
    public String toList() {
        return "usermanage/list";
    }

    //分页查询
    @PostMapping("/getPage")
    @ResponseBody
    public PageResult<User> getPage(@RequestParam(defaultValue = "") String keyword,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer limit) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", keyword.isEmpty() ? null : keyword);
        map.put("page", page);
        map.put("pageSize", limit);
        return userFeignService.getUserByPage(map);
    }

    //删除用户
    @PostMapping("/doDel/{id}")
    @ResponseBody
    public R doDel(@PathVariable("id") Long id) {
        boolean flag = userFeignService.deleteUserById(id);
        return flag ? R.right() : R.error();
    }

    //批量删除
    @PostMapping("/batchDel")
    @ResponseBody
    public R batchDel(@RequestParam(value = "ids[]") Long ids[]) {
        List<Long> list = new ArrayList<>();
        for (Long id : ids) {
            list.add(id);
        }
        boolean flag = userFeignService.deleteByIds(list);
        return flag ? R.right() : R.error();
    }
}
