package com.yuhui.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 文章表(SgArticle)表实体类
 *
 * @author easycode
 * @since 2022-12-31 18:34:48
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sg_article")
@Accessors(chain = true) // 开启链式调用，使set方法返回值为当前实体类类型
public class Article {
    @TableId
    private Long id;
    // 标题
    private String title;
    // 文章内容
    private String content;
    // 文章摘要
    private String summary;
    // 所属分类id
    private Long categoryId;
    // 所属分类name
    @TableField(exist = false)
    private String categoryName;
    // 缩略图
    private String thumbnail;
    // 是否置顶（0 否，1 是）
    private String isTop;
    // 状态（0 已发布，1 草稿）
    private String status;
    // 访问量
    private Long viewCount;
    // 是否允许评论 1是，0否
    private String isComment;
    
    private Long createBy;
    
    private Date createTime;
    
    private Long updateBy;
    
    private Date updateTime;
    // 删除标志（0 代表未删除，1 代表已删除）
    private Integer delFlag;

}

