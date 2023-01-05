package com.yuhui.domain.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 评论表(Comment)表实体类
 *
 * @author makejava
 * @since 2023-01-04 21:02:12
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_comment")
public class Comment  {
    @TableId
    private Long id;
    // 评论类型（0 代表文章评论，1 代表友链评论）
    private String type;
    // 文章id
    private Long articleId;
    // 根评论id
    private Long rootId;
    // 评论内容
    private String content;
    // 所回复的目标评论的userid
    private Long toCommentUserId;
    // 目标评论的id
    private Long toCommentId;

    // 设置插入或更新时自动填充字段...
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    // 删除标志（0 代表未删除，1 代表已删除）
    private Integer delFlag;

}
