package com.yuhui.controller;

import com.yuhui.domain.ResponseResult;
import com.yuhui.service.CommentService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yuhui
 * @date 2023/1/4 22:56
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 分页获取文章评论信息
     * @param articleId 文章id
     * @param pageNum 页码
     * @param pageSize 页面大小
     * @return 评论信息
     */
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return  commentService.commentList(articleId, pageNum, pageSize);
    }
}
