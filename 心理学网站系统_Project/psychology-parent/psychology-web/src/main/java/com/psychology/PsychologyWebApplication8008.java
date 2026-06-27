package com.psychology;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@ServletComponentScan("com.psychology.filter")
public class PsychologyWebApplication8008 {
    public static void main(String[] args) {
        SpringApplication.run(PsychologyWebApplication8008.class, args);
    }
}
