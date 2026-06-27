package com.psychology.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psychology.dao.UserMapper;
import com.psychology.pojo.User;
import com.psychology.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    //根据用户名，密码，角色登录
    public User login(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        queryWrapper.eq("password", user.getPassword());
        queryWrapper.eq("role", user.getRole());
        User user2 = userMapper.selectOne(queryWrapper);
        return user2;
    }

    //用户注册
    public boolean register(User user) {
        //检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User existUser = userMapper.selectOne(queryWrapper);
        if (existUser != null) {
            return false;
        }
        //设置默认值
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        int rows = userMapper.insert(user);
        return rows > 0;
    }
}
