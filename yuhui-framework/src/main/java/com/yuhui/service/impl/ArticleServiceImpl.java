package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.Article;
import com.yuhui.mapper.ArticleMapper;
import com.yuhui.service.ArticleService;
import com.yuhui.utils.BeanCopyUtils;
import com.yuhui.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.yuhui.contants.SystemConstants.ARTICLE_STATUS_NORMAL;

/**
 * @author yuhui
 * @date 2022/12/31 19:43
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

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
}
