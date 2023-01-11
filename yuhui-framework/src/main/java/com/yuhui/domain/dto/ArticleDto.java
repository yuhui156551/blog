package com.yuhui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuhui
 * @date 2023/1/11 14:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    // 标题
    private String title;
    // 摘要
    private String summary;
}
