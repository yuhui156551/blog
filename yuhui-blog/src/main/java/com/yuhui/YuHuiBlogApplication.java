package com.yuhui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yuhui
 * @date 2022/12/31 17:02
 */
@SpringBootApplication
@MapperScan("com.yuhui.mapper")
public class YuHuiBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(YuHuiBlogApplication.class, args);
    }
}
