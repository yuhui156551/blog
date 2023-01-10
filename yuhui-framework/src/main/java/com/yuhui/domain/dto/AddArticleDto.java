package com.yuhui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yuhui
 * @date 2023/1/10 10:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddArticleDto {
    private Long id;
    // 标题
    private String title;
    // 文章内容
    private String content;
    // 文章摘要
    private String summary;
    // 所属分类id
    private Long categoryId;
    // 缩略图
    private String thumbnail;
    // 是否置顶（0 否，1 是）
    private String isTop;
    // 状态（0 已发布，1 草稿）
    private String status;
    // 访问量
    private Long viewCount;
    // 是否允许评论（1 是，0 否）
    private String isComment;
    // 所绑定的标签
    private List<Long> tags;
}
