package com.psychology.feignService;

import com.psychology.pojo.User;
import com.psychology.utils.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@FeignClient(value = "psychology-user-service", path = "/api/user")
public interface UserFeignService {

    @PostMapping("/login")
    public User login(@RequestBody User user);

    @PostMapping("/register")
    public boolean register(@RequestBody User user);

    @PostMapping("/getUserById/{id}")
    public User getUserById(@PathVariable("id") Long id);

    @PostMapping("/updateUser")
    public boolean updateUser(@RequestBody User user);

    @PostMapping("/getUserByPage")
    public PageResult<User> getUserByPage(@RequestBody Map<String, Object> map);

    @PostMapping("/deleteUserById/{id}")
    public boolean deleteUserById(@PathVariable("id") Long id);

    @PostMapping("/deleteByIds")
    public boolean deleteByIds(@RequestBody List<Long> list);
}
