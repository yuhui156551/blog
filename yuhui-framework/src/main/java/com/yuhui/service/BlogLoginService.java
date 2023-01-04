package com.yuhui.service;

import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.User;

/**
 * @author yuhui
 * @date 2023/1/3 19:41
 */
public interface BlogLoginService {
    ResponseResult login(User user);
}
