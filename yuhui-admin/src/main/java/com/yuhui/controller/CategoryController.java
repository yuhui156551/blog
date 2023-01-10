package com.yuhui.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.CategoryDto;
import com.yuhui.domain.dto.CategoryListDto;
import com.yuhui.domain.entity.Category;
import com.yuhui.domain.vo.CategoryListVo;
import com.yuhui.domain.vo.CategoryVo;
import com.yuhui.service.CategoryService;
import com.yuhui.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author yuhui
 * @date 2023/1/10 9:24
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        // 返回状态正常的分类
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> categories = categoryService.list();
        // 封装vo并返回
        return ResponseResult.okResult(BeanCopyUtils.copyBeanList(categories, CategoryVo.class));
    }

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto){
        return categoryService.pageCategoryList(pageNum, pageSize, categoryListDto);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/{id}")
    public ResponseResult selectCategory(@PathVariable("id") Long id){
        return ResponseResult.okResult(BeanCopyUtils.copyBean(categoryService.getById(id), CategoryListVo.class));
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody CategoryListVo categoryListVo){
        return categoryService.updateCategory(categoryListVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * Request URL: http://localhost:81/dev-api/content/category/2,15
     * TODO 批量删除分类
     */
    @DeleteMapping
    public ResponseResult deleteCategoryList(@RequestParam Long[] ids){
        System.out.println(ids);
        return ResponseResult.okResult();
    }
}
