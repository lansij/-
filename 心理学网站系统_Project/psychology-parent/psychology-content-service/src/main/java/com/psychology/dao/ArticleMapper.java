package com.psychology.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.psychology.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
