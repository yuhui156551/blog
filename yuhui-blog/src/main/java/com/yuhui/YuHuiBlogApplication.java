package com.yuhui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author yuhui
 * @date 2022/12/31 17:02
 */
@SpringBootApplication
@MapperScan("com.yuhui.mapper")
@EnableScheduling
@EnableSwagger2
public class YuHuiBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(YuHuiBlogApplication.class, args);
    }
}
