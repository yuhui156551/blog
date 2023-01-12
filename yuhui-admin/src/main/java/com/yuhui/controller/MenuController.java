package com.yuhui.controller;

import com.yuhui.annotation.SystemLog;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.Menu;
import com.yuhui.domain.vo.MenuTreeVo;
import com.yuhui.domain.vo.MenuVo;
import com.yuhui.service.MenuService;
import com.yuhui.utils.BeanCopyUtils;
import com.yuhui.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuhui
 * @date 2023/1/11 19:07
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/treeselect")
    @SystemLog(businessName = "获取菜单下拉树列表")
    public ResponseResult treeSelect() {
        // 获取菜单数据。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.selectMenuList(new Menu());
        // 构造树结构返回给前端显示
        List<MenuTreeVo> options = SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }

    @GetMapping("/list")
    @SystemLog(businessName = "菜单列表")
    public ResponseResult list(Menu menu) {
        List<Menu> menus = menuService.selectMenuList(menu);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @PostMapping
    @SystemLog(businessName = "新增菜单")
    public ResponseResult addMenu(@RequestBody Menu menu){// 其实应该设置dto，奈何我是懒狗
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "回显数据")
    public ResponseResult selectMenu(@PathVariable("id") Long id){
        MenuVo menuVo = BeanCopyUtils.copyBean(menuService.getById(id), MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @PutMapping
    @SystemLog(businessName = "修改菜单")
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        // 上级菜单不能选择自身
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);// 应该防止空值，但是我是后台管理，尽量主观避免吧（其实是因为我懒）
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    @SystemLog(businessName = "删除菜单")
    public ResponseResult deleteMenu(@PathVariable("id") Long id) {
        // 如有子菜单，不能删除
        if (menuService.hasChild(id)) {
            return ResponseResult.errorResult(500,"存在子菜单，无法删除！");
        }
        menuService.removeById(id);
        return ResponseResult.okResult();
    }


}
