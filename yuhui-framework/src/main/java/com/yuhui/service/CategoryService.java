package com.yuhui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.CategoryDto;
import com.yuhui.domain.dto.CategoryListDto;
import com.yuhui.domain.entity.Category;
import com.yuhui.domain.vo.CategoryListVo;
import com.yuhui.domain.vo.CategoryVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-01-01 20:36:04
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto);

    ResponseResult updateCategory(CategoryListVo categoryListVo);

    ResponseResult addCategory(CategoryDto categoryDto);

    List<CategoryVo> listAllCategory();

    void export(HttpServletResponse response);
}
