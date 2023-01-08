package com.yuhui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuhui.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-08 16:15:05
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);
}
