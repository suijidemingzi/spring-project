package com.example.hospital.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long id;             // 用户ID
    private String username;     // 用户名
    private String password;     // 密码
    private String realName;     // 真实姓名
    private String idCard;       // 身份证号
    private String phone;        // 手机号
    private Integer roleType;    // 角色类型 1-患者 2-医生 3-管理员 4-窗口人员
    private Integer isAuth;      // 是否实名认证 0-否 1-是
    private Integer status;      // 状态 0-禁用 1-启用

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 更新时间
}