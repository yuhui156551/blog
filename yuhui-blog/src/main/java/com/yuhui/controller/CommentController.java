package com.yuhui.controller;

import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.Comment;
import com.yuhui.service.CommentService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return  commentService.commentList(SystemConstants.ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    /**
     * 分页获取友链评论信息
     */
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }

    /**
     * 发布评论
     * @param comment 请求体内容
     * @return 200
     */
    @PostMapping
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
}
