package com.yuhui.service.impl;

import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.exception.SystemException;
import com.yuhui.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author yuhui
 * @date 2023/1/11 13:28
 */
@Service("ps")
public class PermissionService {

    /**
     * 判断当前用户是否具有permission
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission){
        // 如果是超级管理员，直接返回true
        if(SecurityUtils.isAdmin()){
            return true;
        }
        // 否则：获取用户权限列表，进行比对
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        // 若为空，抛异常
        if(!StringUtils.hasText((CharSequence) permissions)){
            throw new SystemException(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        return permissions.contains(permission);
    }
}
