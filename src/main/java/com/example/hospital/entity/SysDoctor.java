package com.example.hospital.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_doctor")
public class SysDoctor {
    @TableId(type = IdType.AUTO)
    private Long id;             // 医生ID
    private Long deptId;         // 所属科室ID
    private String doctorName;   // 医生姓名
    private String title;        // 职称
    private String doctorDesc;   // 医生简介
    private String idCard;       // 身份证号
    private String phone;        // 手机号
    private Long accountId;      // 关联账号ID
    private Integer status;      // 状态 0-禁用 1-启用

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 更新时间
}