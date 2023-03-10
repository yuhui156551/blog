package com.yuhui.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuhui
 * @date 2023/1/2 20:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkVo {
    private Long id;
    private String name;
    private String logo;
    private String description;
    private String address;
    private String status;
}
