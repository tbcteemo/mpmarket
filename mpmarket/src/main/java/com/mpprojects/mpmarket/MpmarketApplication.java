package com.mpprojects.mpmarket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mpprojects.mpmarket.dao")
public class MpmarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpmarketApplication.class, args);
    }

}
