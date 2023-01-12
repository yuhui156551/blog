package com.yuhui.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserVo {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 账号状态（0正常 1停用）
     */
    private String status;
    /**
     * 手机号
     */
    private String phonenumber;
    /**
     * 创建时间
     */
    private Date createTime;

}