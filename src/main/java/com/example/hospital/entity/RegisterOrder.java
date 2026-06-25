package com.example.hospital.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("register_order")
public class RegisterOrder {
    @TableId(type = IdType.AUTO)
    private Long id;             // 订单ID
    private String orderNo;      // 订单编号
    private Long patientId;      // 就诊人用户ID
    private Long deptId;         // 科室ID
    private Long doctorId;       // 医生ID
    private Long scheduleId;     // 排班ID
    private LocalDate scheduleDate; // 就诊日期
    private Integer timeType;    // 就诊时段
    private BigDecimal registerFee; // 挂号费
    private Integer payStatus;   // 支付状态 0-待支付 1-已支付 2-已取消 3-已退款
    private LocalDateTime payTime; // 支付时间
    private Integer orderStatus; // 订单状态 1-预约成功 2-已就诊 3-已取消 4-爽约
    private Integer visitNum;    // 就诊序号
    private String idCard;       // 就诊人身份证号
    private String patientName;  // 就诊人姓名
    private String phone;        // 就诊人手机号
    private LocalDateTime cancelTime; // 取消时间

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 更新时间
}