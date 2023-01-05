package com.yuhui.handler.exception;

import com.yuhui.domain.ResponseResult;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 * @author yuhui
 * @date 2023/1/4 18:09
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 自定义异常
     */
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        // 打印异常信息
        log.error("抛出异常哩! {}", e.getMsg());
        // 封装异常信息返回
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    /**
     * 其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        // 打印异常信息
        log.error("出现了异常！ {}", e.getMessage());
        // 从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}
