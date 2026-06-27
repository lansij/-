package com.psychology.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psychology.dao.FavoriteMapper;
import com.psychology.pojo.Favorite;
import com.psychology.service.FavoriteService;
import org.springframework.stereotype.Service;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

}
