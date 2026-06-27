package com.psychology.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 心理文章实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("psy_article")
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    private String cover;

    private String summary;

    private String content;

    private String author;

    private Integer viewCount;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
