package com.yuhui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.domain.entity.Role;
import com.yuhui.mapper.RoleMapper;
import com.yuhui.service.RoleService;
import org.springframework.stereotype.Service;

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
}
