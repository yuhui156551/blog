package com.yuhui.controller;

import com.yuhui.annotation.SystemLog;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.LoginUser;
import com.yuhui.domain.entity.User;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.exception.SystemException;
import com.yuhui.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author yuhui
 * @date 2023/1/3 19:39
 */
@RestController
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    @SystemLog(businessName = "用户登录")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            // 用户名为空
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
