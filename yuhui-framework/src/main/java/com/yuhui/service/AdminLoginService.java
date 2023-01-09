package com.yuhui.service;

import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.User;
import com.yuhui.domain.vo.AdminUserInfoVo;
import com.yuhui.domain.vo.RoutersVo;

/**
 * @author yuhui
 * @date 2023/1/8 14:20
 */
public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult<AdminUserInfoVo> getInfo();

    ResponseResult<RoutersVo> getRouters();

    ResponseResult logout();
}
