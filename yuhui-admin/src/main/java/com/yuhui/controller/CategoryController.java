package com.yuhui.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuhui.annotation.SystemLog;
import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.CategoryDto;
import com.yuhui.domain.dto.CategoryListDto;
import com.yuhui.domain.entity.Category;
import com.yuhui.domain.vo.CategoryListVo;
import com.yuhui.domain.vo.CategoryVo;
import com.yuhui.domain.vo.ExcelCategoryVo;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.service.CategoryService;
import com.yuhui.utils.BeanCopyUtils;
import com.yuhui.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
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
    @SystemLog(businessName = "获取所有分类")
    public ResponseResult listAllCategory(){
        return ResponseResult.okResult(categoryService.listAllCategory());
    }

    @GetMapping("/list")
    @SystemLog(businessName = "分页查询分类列表")
    public ResponseResult list(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto){
        return categoryService.pageCategoryList(pageNum, pageSize, categoryListDto);
    }

    @PostMapping
    @SystemLog(businessName = "新增分类")
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "回显分类数据")
    public ResponseResult selectCategory(@PathVariable("id") Long id){
        return ResponseResult.okResult(BeanCopyUtils.copyBean(categoryService.getById(id), CategoryListVo.class));
    }

    @PutMapping
    @SystemLog(businessName = "修改分类")
    public ResponseResult updateCategory(@RequestBody CategoryListVo categoryListVo){
        return categoryService.updateCategory(categoryListVo);
    }

    /*@DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }*/

    /**
     * Request URL: http://localhost:81/dev-api/content/category/2,15
     */
    @DeleteMapping("/{categoryIds}")
    @SystemLog(businessName = "(批量)删除分类")
    public ResponseResult deleteCategoryList(@PathVariable List<Long> categoryIds){
        categoryService.removeByIds(categoryIds);
        return ResponseResult.okResult();
    }

    @GetMapping("/export")
    @PreAuthorize("@ps.hasPermission('content:category:export')")
//    @SystemLog(businessName = "分类导出") TODO 此处加了报错
    public void export(HttpServletResponse response){
        categoryService.export(response);
    }
}
