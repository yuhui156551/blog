package com.yuhui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuhui
 * @date 2023/1/10 18:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListDto {
    // 分类名
    private String name;
    // 状态
    private String status;
}
