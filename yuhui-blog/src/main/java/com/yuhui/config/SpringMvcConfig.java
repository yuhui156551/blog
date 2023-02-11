package com.yuhui.config;

import com.yuhui.interceptor.IpCountInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yuhui
 * @date 2023/2/11 21:30
 */
@Configuration(proxyBeanMethods = true)// 外部无论对配置类中的这个组件注册方法调用多少次获取的都是之前注册容器中的单实例对象
public class SpringMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipCountInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public IpCountInterceptor ipCountInterceptor(){
        return new IpCountInterceptor();
    }
}
