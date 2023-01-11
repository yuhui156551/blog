package com.yuhui.controller;

import com.yuhui.annotation.SystemLog;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.dto.AddArticleDto;
import com.yuhui.domain.dto.ArticleDto;
import com.yuhui.service.ArticleService;
import com.yuhui.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    @SystemLog(businessName = "分页查询文章列表")
    public ResponseResult list(Integer pageNum, Integer pageSize, ArticleDto articleDto){
        return articleService.pageArticleList(pageNum, pageSize, articleDto);
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "回显文章数据")
    public ResponseResult selectArticle(@PathVariable("id") Long id){
        return ResponseResult.okResult(BeanCopyUtils.copyBean(articleService.getById(id), AddArticleDto.class));
    }

    @PutMapping
    @SystemLog(businessName = "修改文章")
    public ResponseResult updateArticle(@RequestBody AddArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }

    @DeleteMapping("/{id}")
    @SystemLog(businessName = "删除文章")
    public ResponseResult deleteArticle(@PathVariable("id") Long id){
        return ResponseResult.okResult(articleService.removeById(id));
    }

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.add(articleDto);
    }
}