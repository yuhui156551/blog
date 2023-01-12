package com.yuhui.controller;

import com.yuhui.annotation.SystemLog;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.ChangeRoleStatusDto;
import com.yuhui.domain.entity.Role;
import com.yuhui.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuhui
 * @date 2023/1/11 19:50
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;


    /*@GetMapping("/listAllRole")
    @SystemLog(businessName = "获取角色管理列表")
    public ResponseResult listAllRole(){
        List<Role> roles = roleService.selectRoleAll();
        return ResponseResult.okResult(roles);
    }*/

    @GetMapping(value = "/{roleId}")
    public ResponseResult getInfo(@PathVariable Long roleId)
    {
        Role role = roleService.getById(roleId);
        return ResponseResult.okResult(role);
    }

    /**
     * 修改保存角色
     */
    /*@PutMapping
    public ResponseResult edit(@RequestBody Role role)
    {
        roleService.updateRole(role);
        return ResponseResult.okResult();
    }*/

    @DeleteMapping("/{id}")
    @SystemLog(businessName = "删除角色")
    public ResponseResult remove(@PathVariable("id") Long id) {
        roleService.removeById(id);
        return ResponseResult.okResult();
    }


    /**
     * 新增角色
     */
    /*@PostMapping
    public ResponseResult add( @RequestBody Role role)
    {
        roleService.insertRole(role);
        return ResponseResult.okResult();

    }*/
    @GetMapping("/list")
    @SystemLog(businessName = "角色管理列表")
    public ResponseResult list(Role role, Integer pageNum, Integer pageSize) {
        return roleService.pageRoleList(role,pageNum,pageSize);
    }

    @PutMapping("/changeStatus")
    @SystemLog(businessName = "修改角色状态")
    public ResponseResult changeStatus(@RequestBody ChangeRoleStatusDto roleStatusDto){
        Role role = new Role();
        role.setId(roleStatusDto.getRoleId());
        role.setStatus(roleStatusDto.getStatus());
        // 只修改实体对象中有值的字段
        // eg：UPDATE sys_role SET status=?, update_by=?, update_time=? WHERE id=? AND del_flag='0'
        return ResponseResult.okResult(roleService.updateById(role));
    }

}
