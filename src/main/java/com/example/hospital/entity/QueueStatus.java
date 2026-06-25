package com.example.hospital.entity;

import lombok.Data;

@Data
public class QueueStatus {
    private String deptName;              // 诊室名称 (如：内科诊室)
    private Integer currentCallingNumber;  // 当前叫号 (如：2)
    private Integer userQueueNumber;       // 您的排队号 (如：3)
    private Integer waitingCount;          // 前面等待人数 (如：0)
}