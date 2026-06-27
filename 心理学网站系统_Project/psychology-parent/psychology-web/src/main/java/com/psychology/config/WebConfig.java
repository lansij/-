package com.psychology.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectPath = System.getProperty("user.dir");
        // 上传图片目录 + classpath静态资源目录
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:" + projectPath + "/uploads/img/", "classpath:/static/img/");
        // 心理文章图片目录
        registry.addResourceHandler("/article-img/**")
                .addResourceLocations("file:" + projectPath + "/../心理文章/");
    }
}
