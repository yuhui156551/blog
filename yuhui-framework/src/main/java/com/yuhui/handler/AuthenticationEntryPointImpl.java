package com.yuhui.handler;

import com.alibaba.fastjson.JSON;
import com.yuhui.domain.ResponseResult;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器：
 * 目前我们的项目在认证出错或者权限不足的时候响应回来的Json是Security的异常处理结果。
 * 但是这个响应的格式是不符合我们项目的接口规范的。
 * 所以需要自定义异常处理。
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        ResponseResult result = null;
        if(authException instanceof BadCredentialsException){
            // BadCredentialsException 用户名或密码错误抛出的异常
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),authException.getMessage());
        }else if(authException instanceof InsufficientAuthenticationException){
            // InsufficientAuthenticationException 权限不足抛出的异常
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else{
            // 其他异常
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证失败");
        }
        // 响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}