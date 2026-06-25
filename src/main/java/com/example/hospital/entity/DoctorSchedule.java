package com.example.hospital.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("doctor_schedule")
public class DoctorSchedule {
    @TableId(type = IdType.AUTO)
    private Long id;             // 排班ID
    private Long deptId;         // 科室ID
    private Long doctorId;       // 医生ID
    private LocalDate scheduleDate; // 出诊日期
    private Integer timeType;    // 时段 1-上午 2-下午 3-夜间
    private Integer totalNum;    // 总号源数
    private Integer remainNum;   // 剩余号源数
    private BigDecimal registerFee; // 挂号费
    private Integer status;      // 状态 0-停诊 1-可预约
    @Version
    private Integer version;     // 乐观锁版本号

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 更新时间
}