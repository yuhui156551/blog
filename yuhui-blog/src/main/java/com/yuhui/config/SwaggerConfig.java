package com.yuhui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    // 返回的是Doket对象，通过这个对象描述文档信息
    public Docket createRestApi() {
    // 文档类型
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            // 控制层的包路径，每一个方法都会映射成一个接口
            .apis(RequestHandlerSelectors.basePackage("com.yuhui.controller"))
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("馀灰不是鱼灰", "http://www.yuhui.com", "536430462@qq.com");
        return new ApiInfoBuilder()
                .title("馀灰博客接口文档")
                .description("swagger接口文档")
                .contact(contact)   // 联系方式
                .version("1.1.0")  // 版本
                .build();
    }
}