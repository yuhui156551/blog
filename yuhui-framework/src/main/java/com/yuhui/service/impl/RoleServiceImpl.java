package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.Role;
import com.yuhui.domain.vo.PageVo;
import com.yuhui.mapper.RoleMapper;
import com.yuhui.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-01-08 16:15:06
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        // 如果是管理员，返回的集合中只需要有“admin”
        if(id == 1L){
            List roleKeyList = new ArrayList<>();
            roleKeyList.add("admin");
            return roleKeyList;
        }
        // 否则，查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult pageRoleList(Role role, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        // 根据名称、状态查询
        queryWrapper.like(StringUtils.hasText(role.getRoleName()), Role::getRoleName, role.getRoleName());
        queryWrapper.eq(StringUtils.hasText(role.getStatus()), Role::getStatus, role.getStatus());
        // 根据sort排序
        queryWrapper.orderByAsc(Role::getRoleSort);
        // 分页查询
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        // 封装vo返回
        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }
}
