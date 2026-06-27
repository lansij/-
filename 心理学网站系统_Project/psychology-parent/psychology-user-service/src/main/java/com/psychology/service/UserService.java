package com.psychology.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.psychology.pojo.User;

public interface UserService extends IService<User> {

    //根据用户名，密码登录
    public User login(User user);

    //用户注册
    public boolean register(User user);
}
