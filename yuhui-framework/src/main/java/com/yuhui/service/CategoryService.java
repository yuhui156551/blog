package com.yuhui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-01-01 20:36:04
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}
