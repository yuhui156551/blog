package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.entity.LoginUser;
import com.yuhui.domain.entity.User;
import com.yuhui.mapper.MenuMapper;
import com.yuhui.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author yuhui
 * @date 2023/1/3 20:20
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        // 判断是否查到用户  如果没有查到 抛出异常
        if(Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        // 返回用户信息
        // 查询权限信息封装，注意：后台用户才需要权限
        if(user.getType().equals(SystemConstants.ADMIN)){
            List<String> permissions = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user, permissions);
        }
        return new LoginUser(user, null);
    }
}
