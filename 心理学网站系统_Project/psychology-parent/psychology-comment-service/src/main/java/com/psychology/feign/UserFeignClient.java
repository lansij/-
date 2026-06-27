package com.psychology.feign;

import com.psychology.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 调用用户服务获取用户信息（用于评论展示昵称和头像）
 */
@FeignClient(value = "psychology-user-service", path = "/api/user")
public interface UserFeignClient {

    @PostMapping("/getUserById/{id}")
    public User getUserById(@PathVariable("id") Long id);
}
