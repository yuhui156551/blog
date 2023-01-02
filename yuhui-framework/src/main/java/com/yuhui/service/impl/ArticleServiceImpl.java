package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.Article;
import com.yuhui.mapper.ArticleMapper;
import com.yuhui.service.ArticleService;
import com.yuhui.service.CategoryService;
import com.yuhui.utils.BeanCopyUtils;
import com.yuhui.vo.ArticleListVo;
import com.yuhui.vo.HotArticleVo;
import com.yuhui.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yuhui.contants.SystemConstants.ARTICLE_STATUS_NORMAL;

/**
 * @author yuhui
 * @date 2022/12/31 19:43
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult hotArticleList() {
        // 构造条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 正式文章
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        // 浏览量进行降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        // 十条数据
        Page<Article> page = new Page(1, 10);
        page(page, queryWrapper);
        // 获取数据
        List<Article> articles = page.getRecords();
        // bean拷贝，封装成vo对象脱敏
//        ArrayList<HotArticleVo> articleVos = new ArrayList<>();
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article, vo);
//            articleVos.add(vo);
//        }
        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        // 返回
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 构造查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 若前端传了categoryId，则表示是分类下的查询，没传则是主页的查询
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0, Article::getCategoryId, categoryId);
        // 状态是正文
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        // 优先查询置顶文章，方案：根据isTop降序查询
        queryWrapper.orderByDesc(Article::getIsTop);
        // 分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        // 分页查询结果
        List<Article> articles = page.getRecords();
        // 查询categoryName，如果看不懂，可以参考下方注释里面的增强for形式
        articles = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }
        // 封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        // 返回
        return ResponseResult.okResult(pageVo);
    }
}
