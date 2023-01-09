package com.yuhui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author yuhui
 * @date 2023/1/9 18:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagListDto {
    // 标签名
    private String name;
    // 备注
    private String remark;
}
