package com.psychology;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.psychology.dao")
public class PsychologyUserApplication8101 {
    public static void main(String[] args) {
        SpringApplication.run(PsychologyUserApplication8101.class, args);
    }
}
