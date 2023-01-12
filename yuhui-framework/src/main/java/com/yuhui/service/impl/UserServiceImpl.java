package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.User;
import com.yuhui.domain.entity.UserRole;
import com.yuhui.domain.vo.PageVo;
import com.yuhui.domain.vo.UserVo;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.exception.SystemException;
import com.yuhui.mapper.UserMapper;
import com.yuhui.service.UserRoleService;
import com.yuhui.service.UserService;
import com.yuhui.utils.BeanCopyUtils;
import com.yuhui.utils.SecurityUtils;
import com.yuhui.domain.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-01-05 12:48:41
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public ResponseResult userInfo() {
        // 获取当前用户id
        Long userId = SecurityUtils.getUserId();
        // 查询用户信息
        User user = getById(userId);
        // 封装Vo返回
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        // 对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        // 用户名、昵称不能重复
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        // 加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        // 保存
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult pageUserList(User user, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 可以根据用户名模糊搜索
        queryWrapper.like(StringUtils.hasText(user.getUserName()), User::getUserName, user.getUserName());
        // 可以进行手机号的搜索
        queryWrapper.eq(StringUtils.hasText(user.getPhonenumber()), User::getPhonenumber, user.getPhonenumber());
        // 可以进行状态的查询
        queryWrapper.eq(StringUtils.hasText(user.getStatus()), User::getStatus, user.getStatus());
        // 分页查询
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        // User无效字段太多，转成UserVo
        List<User> users = page.getRecords();
        List<UserVo> userVos = BeanCopyUtils.copyBeanList(users, UserVo.class);
        /*List<UserVo> userVoList = users.stream()
                .map(u -> BeanCopyUtils.copyBean(u, UserVo.class))
                .collect(Collectors.toList());*/
        // 转换成pageVo返回
        return ResponseResult.okResult(new PageVo(userVos, page.getTotal()));
    }

    @Override
    public ResponseResult addUser(User user) {
        // 为空和是否唯一判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!checkUserNameUnique(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!checkPhoneUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!checkEmailUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        // 密码加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        // 判断用户是否含有关联角色id
        if (user.getRoleIds() != null && user.getRoleIds().length > 0) {
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId, user.getId());
        userRoleService.remove(userRoleUpdateWrapper);
        // 新增用户与角色管理
        insertUserRole(user);
        // 手机号、邮箱不能重复
        if (!checkPhoneUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!checkEmailUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        // 更新用户信息
        updateById(user);
    }

    /**
     * 插入user_role表
     * @param user 用户信息
     */
    private void insertUserRole(User user) {
        List<UserRole> userRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
    }

    private boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName, userName)) == 0;
    }

    public boolean checkPhoneUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber, user.getPhonenumber())) == 0;
    }

    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail, user.getEmail())) == 0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        return count(queryWrapper.eq(User::getNickName, nickName)) > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        return count(queryWrapper.eq(User::getUserName, userName)) > 0;
    }
}
