package com.yuhui.annotation;

import org.aspectj.lang.annotation.Around;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yuhui
 * @date 2023/1/7 9:52
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SystemLog {
    String businessName();
}
