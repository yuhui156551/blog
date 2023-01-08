package com.yuhui.service;

import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.User;

/**
 * @author yuhui
 * @date 2023/1/8 14:20
 */
public interface AdminLoginService {
    ResponseResult login(User user);
}
