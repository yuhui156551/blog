package com.yuhui.domain.vo;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuhui
 * @date 2023/1/10 18:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListVo {
    private Long id;
    private String name;
    private String description;
    private String status;
}
