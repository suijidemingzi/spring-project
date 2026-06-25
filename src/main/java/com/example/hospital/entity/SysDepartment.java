package com.example.hospital.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_department")
public class SysDepartment {
    @TableId(type = IdType.AUTO)
    private Long id;             // 科室ID
    private String deptName;     // 科室名称
    private String deptCode;     // 科室编码
    private String deptDesc;     // 科室描述
    private Integer sort;        // 排序
    private Integer status;      // 状态 0-禁用 1-启用

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 更新时间
}