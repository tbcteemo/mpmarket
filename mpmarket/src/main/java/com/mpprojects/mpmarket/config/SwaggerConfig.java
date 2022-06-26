package com.mpprojects.mpmarket.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    //默认分组
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .build();
    }

    @Bean
    public Docket userSystemGroup(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("用户及管理员系统")
                .select()
//                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.mpprojects.mpmarket.controller.user"))
                .build();
    }

    @Bean
    public Docket productSystem(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("商品系统")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mpprojects.mpmarket.controller.shop.productSystem"))
                .build();
    }

    @Bean
    public Docket couponSystem(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("优惠券系统")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mpprojects.mpmarket.controller.shop.couponSystem"))
                .build();
    }

    @Bean
    public Docket shoppingSystem(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("购物系统")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mpprojects.mpmarket.controller.shop.shoppingSystem"))
                .build();
    }

    @Bean
    public Docket relationshipSystem(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("中间表管理")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mpprojects.mpmarket.controller.shop.relationship"))
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
