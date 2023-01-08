package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.entity.Menu;
import com.yuhui.mapper.MenuMapper;
import com.yuhui.service.MenuService;
import org.springframework.stereotype.Service;

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
        if(id == 1L){
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
}
