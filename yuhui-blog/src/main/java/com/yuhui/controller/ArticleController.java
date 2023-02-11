package com.yuhui.controller;

import cn.yuhui.service.IpCountService;
import com.yuhui.annotation.SystemLog;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.Article;
import com.yuhui.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yuhui
 * @date 2022/12/31 19:48
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Resource
    private IpCountService ipCountService;

    @GetMapping("/hotArticleList")
    @SystemLog(businessName = "获取热门文章")
    public ResponseResult hotArticleList() {
        // 自定义ip计数starter
//        ipCountService.count();
        return articleService.hotArticleList();
    }

    @GetMapping("/articleList")
    @SystemLog(businessName = "分页查询文章")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.articleList(pageNum, pageSize, categoryId);
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "文章详情页")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    /**
     * 需求：
     * ①在应用启动时把博客的浏览量存储到redis中
     * ②更新浏览量时去更新redis中的数据
     * ③每隔10分钟把Redis中的浏览量更新到数据库中
     * ④读取文章浏览量时从redis读取
     */
    @PutMapping("/updateViewCount/{id}")
    @SystemLog(businessName = "更新浏览量")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
