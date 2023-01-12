package com.yuhui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-01-05 12:48:41
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult pageUserList(User user, Integer pageNum, Integer pageSize);

    ResponseResult addUser(User user);

    void updateUser(User user);
}
