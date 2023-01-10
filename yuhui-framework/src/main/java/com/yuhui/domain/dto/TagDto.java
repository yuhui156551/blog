package com.yuhui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuhui
 * @date 2023/1/10 9:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    // id
    private Long id;
    // 标签名
    private String name;
    // 备注
    private String remark;
}
