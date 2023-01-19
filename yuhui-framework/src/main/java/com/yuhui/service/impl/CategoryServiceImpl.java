package com.yuhui.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.CategoryDto;
import com.yuhui.domain.dto.CategoryListDto;
import com.yuhui.domain.entity.Article;
import com.yuhui.domain.entity.Category;
import com.yuhui.domain.entity.Tag;
import com.yuhui.domain.vo.CategoryListVo;
import com.yuhui.domain.vo.ExcelCategoryVo;
import com.yuhui.domain.vo.PageVo;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.exception.SystemException;
import com.yuhui.mapper.CategoryMapper;
import com.yuhui.service.ArticleService;
import com.yuhui.service.CategoryService;
import com.yuhui.utils.BeanCopyUtils;
import com.yuhui.domain.vo.CategoryVo;
import com.yuhui.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-01-01 20:36:04
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        // 查询状态为已发布的文章表
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);
        // 从文章表获取文章分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        // 查询状态正常的分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        // 封装vo返回
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto) {
        // 根据状态进行查询
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(categoryListDto.getStatus()), Category::getStatus, categoryListDto.getStatus());
        // 根据分类名进行模糊查询
        queryWrapper.like(StringUtils.hasText(categoryListDto.getName()), Category::getName, categoryListDto.getName());
        // 分页查询
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        // 封装成pageVo返回
        List<CategoryListVo> categoryListVos = BeanCopyUtils.copyBeanList(page.getRecords(), CategoryListVo.class);
        return ResponseResult.okResult(new PageVo(categoryListVos, page.getTotal()));
    }

    @Override
    public ResponseResult updateCategory(CategoryListVo categoryListVo) {
        // 内容不能为空
        if (!StringUtils.hasText(categoryListVo.getName())
                || !StringUtils.hasText(categoryListVo.getDescription())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        // 根据id获取旧数据
        Category oldCategory = getById(categoryListVo.getId());
        // 若传递过来的数据和旧数据不一样，说明进行了修改
        if (!oldCategory.getName().equals(categoryListVo.getName())) {
            // 判断分类名是否重复
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            if (count(queryWrapper.eq(Category::getName, categoryListVo.getName())) > 0) {
                throw new SystemException(AppHttpCodeEnum.CATEGORY_NAME_EXIST);
            }
        }
        // 修改数据
        Category category = BeanCopyUtils.copyBean(categoryListVo, Category.class);
        // 根据id更新
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId, categoryListVo.getId());
        update(category, queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        // 内容不能为空
        if (!StringUtils.hasText(categoryDto.getName())
                || !StringUtils.hasText(categoryDto.getDescription())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        // 判断分类名是否重复
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        if (count(queryWrapper.eq(Category::getName, categoryDto.getName())) > 0) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NAME_EXIST);
        }
        // 设置值并保存
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        // 返回状态正常的分类
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> categories = list();
        // 封装vo并返回
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return categoryVos;
    }

    @Override
    public void export(HttpServletResponse response) {
        try {
            // 设置参数
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            // 获取数据
            List<Category> categorys = list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categorys, ExcelCategoryVo.class);
            // 写入excel
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class)
                    .autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
//            throw new RuntimeException(e);
            // 如果出现异常，返回json
            // 重置response
//            response.reset();
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
