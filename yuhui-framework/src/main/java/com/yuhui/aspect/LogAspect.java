package com.yuhui.aspect;

import com.alibaba.fastjson.JSON;
import com.yuhui.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 切面类
 * @author yuhui
 * @date 2023/1/7 9:58
 */
@Component
@Aspect// 标识为切面类
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.yuhui.annotation.SystemLog)")// 切点
    public void pt(){
    }

    @Around("pt()")// 制作切面
    public Object printLog(ProceedingJoinPoint pjp) throws Throwable {
        Object ret;
        try {
            handleBefore(pjp);
            long startTime = System.currentTimeMillis();
            // 此处要选择抛出异常而非catch，否则异常都在此处catch，统一异常处理失效
            ret = pjp.proceed();
            log.info("Time is        : {}ms", System.currentTimeMillis() - startTime);
            handleAfter(ret);
        } finally {
            // 结束后换行
            log.info("=======End=========" + System.lineSeparator());
        }
        return ret;
    }

    /**
     * 原始方法执行前打印log
     *
     * @param pjp 连接点对象
     */
    private void handleBefore(ProceedingJoinPoint pjp) {
        // 获取同一线程下的request
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(pjp);

        log.info("=======Start=======");
        log.info("URL            : {}", request.getRequestURL());
        log.info("BusinessName   : {}", systemLog.businessName());
        log.info("HTTP Method    : {}", request.getMethod());
        log.info("Class Method   : {}.{}", pjp.getSignature().getDeclaringTypeName(), pjp.getSignature().getName());
        log.info("IP             : {}", request.getRemoteHost());
        log.info("Request Args   : {}", JSON.toJSONString(pjp.getArgs()));// 数组转成json格式
    }

    /**
     * 原始方法执行后打印log
     * @param ret 原始方法执行后的返回对象
     */
    private void handleAfter(Object ret) {
        log.info("Response       : {}", JSON.toJSONString(ret));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint pjp) {
        // 获取注解对应的的方法并封装成一个对象
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        // 获取方法上的注解
        SystemLog systemLog = method.getAnnotation(SystemLog.class);
        return systemLog;
    }
}
