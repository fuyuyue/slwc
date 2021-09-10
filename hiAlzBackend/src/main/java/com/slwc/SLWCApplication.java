package com.slwc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.slwc.dao")
public class SLWCApplication {

    public static void main(String[] args) {
        SpringApplication.run(SLWCApplication.class, args);
    }

}
