package com.yuhui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.Article;

/**
 * @author yuhui
 * @date 2022/12/31 19:42
 */
public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);
}
