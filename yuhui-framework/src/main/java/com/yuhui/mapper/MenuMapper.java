package com.yuhui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuhui.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-08 16:09:05
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

    /**
     * 返回所有菜单
     */
    List<Menu> selectAllRouterMenu();

    /**
     * 根据用户id->角色id->获取对应菜单
     */
    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    /**
     * 根据角色id获取菜单id的集合
     */
    List<Long> selectMenuListByRoleId(Long roleId);
}
