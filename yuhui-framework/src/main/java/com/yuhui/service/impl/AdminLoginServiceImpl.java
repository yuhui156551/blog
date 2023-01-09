package com.yuhui.service.impl;

import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.LoginUser;
import com.yuhui.domain.entity.Menu;
import com.yuhui.domain.entity.User;
import com.yuhui.service.AdminLoginService;
import com.yuhui.service.MenuService;
import com.yuhui.service.RoleService;
import com.yuhui.utils.BeanCopyUtils;
import com.yuhui.utils.JwtUtil;
import com.yuhui.utils.RedisCache;
import com.yuhui.utils.SecurityUtils;
import com.yuhui.vo.AdminUserInfoVo;
import com.yuhui.vo.BlogUserLoginVo;
import com.yuhui.vo.RoutersVo;
import com.yuhui.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yuhui
 * @date 2023/1/8 14:20
 */
@Service
public class AdminLoginServiceImpl implements AdminLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        // 进行用户认证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 判断认证是否通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        // 获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtil.createJWT(userId);
        // 用户信息存入Redis
        redisCache.setCacheObject(SystemConstants.ADMIN_LOGIN_KEY + userId, loginUser);
//        // 封装token UserInfo 返回
//        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
//        BlogUserLoginVo vo = new BlogUserLoginVo(token, userInfoVo);
//        return ResponseResult.okResult(vo);
        // 把token封装 返回
        Map<String,String> map = new HashMap<>();
        map.put("token", token);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult<AdminUserInfoVo> getInfo() {
        // 获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        // 根据id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        // 根据id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        // 获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        // 封装返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @Override
    public ResponseResult<RoutersVo> getRouters() {
        // 获取用户id
        Long userId = SecurityUtils.getUserId();
        // 根据id查询menu数据
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        // 封装返回
        RoutersVo routersVo = new RoutersVo(menus);
        return ResponseResult.okResult(routersVo);
    }

    @Override
    public ResponseResult logout() {
        // 获取userid，删除redis里面的用户信息
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject(SystemConstants.ADMIN_LOGIN_KEY + userId);
        // 返回
        return ResponseResult.okResult();
    }
}
