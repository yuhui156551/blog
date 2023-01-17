package com.yuhui.controller;

import com.yuhui.annotation.SystemLog;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.User;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.exception.SystemException;
import com.yuhui.service.AdminLoginService;
import com.yuhui.domain.vo.AdminUserInfoVo;
import com.yuhui.domain.vo.RoutersVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author yuhui
 * @date 2023/1/8 14:16
 */
@RestController
public class AdminLoginController {

    @Autowired
    private AdminLoginService adminLoginService;

    /**
     * 登录
     * ①自定义登录接口
     *      调用ProviderManager的方法进行认证 如果认证通过生成jwt
     *      把用户信息存入redis中
     * ②自定义UserDetailsService
     *      在这个实现类中去查询数据库
     *      配置passwordEncoder为BCryptPasswordEncoder
     * 校验：
     * ①定义Jwt认证过滤器
     *      获取token
     *      解析token获取其中的userid
     *      从redis中获取用户信息
     *      存入SecurityContextHolder
     */
    @PostMapping("/user/login")
    @SystemLog(businessName = "用户登录")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            // 用户名为空
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }

    @PostMapping("/user/logout")
    @SystemLog(businessName = "用户登出")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }

    @GetMapping("/getInfo")
    @SystemLog(businessName = "获取用户信息")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        return adminLoginService.getInfo();
    }

    @GetMapping("/getRouters")
    @SystemLog(businessName = "获取菜单")
    public ResponseResult<RoutersVo> getRouters(){
        return adminLoginService.getRouters();
    }
}
