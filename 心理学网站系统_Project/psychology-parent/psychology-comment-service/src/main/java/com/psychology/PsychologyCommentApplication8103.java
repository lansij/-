package com.psychology;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.psychology.dao")
@EnableFeignClients
public class PsychologyCommentApplication8103 {
    public static void main(String[] args) {
        SpringApplication.run(PsychologyCommentApplication8103.class, args);
    }
}
