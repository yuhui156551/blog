package com.yuhui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-01-04 21:02:13
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
