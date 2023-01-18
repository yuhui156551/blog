package com.yuhui.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuhui
 * @date 2023/1/18 15:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleView {
    private Long id;
    private Integer viewCount;
}
