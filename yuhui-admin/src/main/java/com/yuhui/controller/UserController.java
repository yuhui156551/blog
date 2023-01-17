package com.yuhui.controller;

import com.yuhui.annotation.SystemLog;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.UserDto;
import com.yuhui.domain.entity.Role;
import com.yuhui.domain.entity.User;
import com.yuhui.domain.vo.UserInfoAndRoleIdsVo;
import com.yuhui.service.RoleService;
import com.yuhui.service.UserService;
import com.yuhui.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuhui
 * @date 2023/1/12 12:00
 */
@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    @SystemLog(businessName = "分页查询用户信息")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize) {
        return userService.pageUserList(user, pageNum, pageSize);
    }

    @PostMapping
    @SystemLog(businessName = "新增用户")
    public ResponseResult add(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/{userId}")
    @SystemLog(businessName = "回显数据")
    public ResponseResult getUserInfoAndRoleIds(@PathVariable("userId") Long userId) {
        // 获取所有角色
        List<Role> roles = roleService.selectRoleAll();
        // 根据id获取用户数据
        User user = userService.getById(userId);
        // 获取当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);
        // 封装vo，返回用户数据、所有角色数据、此用户关联的角色id
        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user, roles, roleIds);
        return ResponseResult.okResult(vo);
    }

    @PutMapping
    @SystemLog(businessName = "修改用户信息")
    public ResponseResult updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{userIds}")
    @SystemLog(businessName = "(批量)删除用户")
    public ResponseResult remove(@PathVariable List<Long> userIds) {
        // 注意：无法删除当前正在使用的用户
        if(userIds.contains(SecurityUtils.getUserId())){
            return ResponseResult.errorResult(500,"不能删除当前你正在使用的用户");
        }
        // 批量删除
        userService.removeByIds(userIds);
        return ResponseResult.okResult();
    }

    @PutMapping("/changeStatus")
    @SystemLog(businessName = "修改用户状态")
    public ResponseResult changeStatus(@RequestBody UserDto userDto){
        User user = new User();
        user.setId(userDto.getUserId());
        user.setStatus(userDto.getStatus());
        userService.updateById(user);
        return ResponseResult.okResult();
    }
}
