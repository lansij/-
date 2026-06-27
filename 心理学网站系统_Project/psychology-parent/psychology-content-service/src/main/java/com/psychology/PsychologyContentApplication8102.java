package com.psychology;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.psychology.dao")
public class PsychologyContentApplication8102 {
    public static void main(String[] args) {
        SpringApplication.run(PsychologyContentApplication8102.class, args);
    }
}
