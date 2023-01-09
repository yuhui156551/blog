package com.yuhui.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yuhui
 * @date 2023/1/2 19:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    private Long id;
    // 标题
    private String title;
    // 文章摘要
    private String summary;
    // 所属分类id
    private Long categoryId;
    // 所属分类名
    private String categoryName;
    // 缩略图
    private String thumbnail;
    // 文章内容
    private String content;
    // 访问量
    private Long viewCount;
    // 时间
    private Date createTime;
}
