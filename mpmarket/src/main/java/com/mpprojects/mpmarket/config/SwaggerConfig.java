package com.mpprojects.mpmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
            .title("MybatisPlus商城测试")
            .description("放弃JPA，使用mybatis框架重新做的电子商城测试")
            .version("V1.0.0")
            .contact(new Contact("陈卓","www.rayzor.top","tbcteemo@gmail.com"))
            .build();

    }
}
