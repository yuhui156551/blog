package com.yuhui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhui.contants.SystemConstants;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.entity.Comment;
import com.yuhui.enums.AppHttpCodeEnum;
import com.yuhui.exception.SystemException;
import com.yuhui.mapper.CommentMapper;
import com.yuhui.service.CommentService;
import com.yuhui.service.UserService;
import com.yuhui.utils.BeanCopyUtils;
import com.yuhui.vo.CommentVo;
import com.yuhui.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-01-04 21:02:13
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        // 查询文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        // 当commentType为0的时候(文章评论)，才加入articleId这个条件
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);
        // 评论类型
        queryWrapper.eq(Comment::getType, commentType);
        // rootId为-1
        queryWrapper.eq(Comment::getRootId, -1);
        // 分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        // Comment没有toCommentUserName和username这两个个字段
        // 而前端需要，而且需要去除一些字段，所以需要封装成Vo
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
        // 查询根评论对应的子评论
        for (CommentVo commentVo : commentVoList) {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        // 封装成PageVo并返回
        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        // 评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        // 保存并返回
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 封装成CommentVo集合
     * @param list Comment集合
     * @return
     */
    private List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVoList = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        // 遍历Vo集合
        for (CommentVo commentVo : commentVoList) {
            // 获取用户昵称
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            // 获取toCommentUserName，如果toCommentUserId不为-1才进行查询
            // toCommentUserId为-1表明是此评论根评论
            if (commentVo.getToCommentUserId() != -1) {
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        // 返回
        return commentVoList;
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        // 根据rootId查找子评论，并进行排序
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        // 所有子评论封装成Vo
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }
}
