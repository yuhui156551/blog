package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.entity.Menu;
import com.yuhui.mapper.MenuMapper;
import com.yuhui.service.MenuService;
import com.yuhui.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-01-08 16:09:06
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        // 若用户id为1（yh），代表是超级管理员，返回所有权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            // 菜单 按钮
            queryWrapper.in(Menu::getMenuType, "C", "F");// 本该定义为常量，这里我偷懒了
            // 状态为正常
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            // 获取权限
            List<Menu> menuList = list(queryWrapper);
            List<String> perms = menuList.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        // 否则返回其具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        if (SecurityUtils.isAdmin()) {
            // 如果是管理员，返回符合要求的menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
            // 否则，返回所具有的menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        // 构建tree结构
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;
    }

    /**
     * 获取菜单列表
     * @param menu 传入的状态或菜单名
     * @return
     */
    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        // menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()), Menu::getMenuName, menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()), Menu::getStatus, menu.getStatus());
        // 排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);
        return menus;
    }

    /**
     * 根据菜单id判断是否含有子菜单
     * @param id 菜单id
     * @return
     */
    @Override
    public boolean hasChild(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, id);
        return count(queryWrapper) > 0;
    }

    /**
     * 构建tree
     */
    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))// 根据0L过滤出父级菜单
                .map(menu -> menu.setChildren(getChildren(menu, menus)))// 给子菜单赋值
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取menu的子Menu集合
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))// 根据父id过滤出二级菜单
                .map(m -> m.setChildren(getChildren(m, menus)))// 递归给子菜单赋值
                .collect(Collectors.toList());
        return childrenList;
    }
}
