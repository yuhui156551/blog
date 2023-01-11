package com.yuhui.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuhui
 * @date 2023/1/1 21:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelCategoryVo {
    @ExcelProperty("分类名")
    private String name;
    @ExcelProperty("描述")
    private String description;
    @ExcelProperty("状态: 0正常,1禁用")
    private String status;
}
