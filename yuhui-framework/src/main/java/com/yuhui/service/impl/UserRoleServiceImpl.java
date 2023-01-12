package com.yuhui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.domain.entity.UserRole;
import com.yuhui.mapper.UserRoleMapper;
import com.yuhui.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-01-12 12:59:57
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
