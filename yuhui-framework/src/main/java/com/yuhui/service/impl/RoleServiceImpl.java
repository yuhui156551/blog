package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.Role;
import com.yuhui.domain.entity.RoleMenu;
import com.yuhui.domain.vo.PageVo;
import com.yuhui.mapper.RoleMapper;
import com.yuhui.service.RoleMenuService;
import com.yuhui.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-01-08 16:15:06
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleMenuService roleMenuService;

    /**
     * 根据用户id获取roleKey
     * @param id userId
     * @return
     */
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

    @Override
    @Transactional
    public void insertRole(Role role) {
        save(role);
        // 如果有菜单值，则插入role_menu表
        if(role.getMenuIds() != null && role.getMenuIds().length > 0){
            insertRoleMenu(role);
        }
    }

    @Override
    public void updateRole(Role role) {
        // 先删除之前的role_menu表当中的数据，再插入role_menu表
        updateById(role);
        roleMenuService.deleteRoleMenuByRoleId(role.getId());
        insertRoleMenu(role);
    }

    @Override
    public List<Role> selectRoleAll() {
        // 状态：正常
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        return list(queryWrapper);
    }

    @Override
    public List<Long> selectRoleIdByUserId(Long userId) {
        return getBaseMapper().selectRoleIdByUserId(userId);
    }

    /**
     * 插入role_menu表
     * @param role 角色数据
     */
    private void insertRoleMenu(Role role){
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }
}
