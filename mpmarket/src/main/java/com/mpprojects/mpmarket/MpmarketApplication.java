package com.mpprojects.mpmarket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.mpprojects.mpmarket.dao")
@EnableSwagger2
public class MpmarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpmarketApplication.class, args);
    }

}
