package com.yuhui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuhui
 * @date 2023/1/11 18:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkStatusDto {
    private Long id;
    private String status;
}
