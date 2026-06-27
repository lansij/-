package com.psychology.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.psychology.dao.NewsMapper;
import com.psychology.pojo.News;
import com.psychology.service.NewsService;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

}
