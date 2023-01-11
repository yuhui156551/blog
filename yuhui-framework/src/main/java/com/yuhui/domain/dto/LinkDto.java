package com.yuhui.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuhui
 * @date 2023/1/11 17:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {
    private Long id;
    private String name;
    private String logo;
    private String description;
    private String address;
    private String status;
}
