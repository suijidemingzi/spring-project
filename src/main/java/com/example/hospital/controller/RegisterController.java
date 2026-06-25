package com.example.hospital.controller;

import com.example.hospital.common.Result;
import com.example.hospital.entity.QueueStatus;
import com.example.hospital.entity.RegisterOrder;
import com.example.hospital.service.RegisterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

// 恢复类注解，去掉 Knife4j 的 @Tag
@RestController
@RequestMapping("/register")
public class RegisterController {

    // 恢复 registerService 注入
    @Resource
    private RegisterService registerService;

    // 恢复提交挂号方法，去掉 Knife4j 的 @Operation、@Parameter
    @PostMapping("/submit/{patientId}/{scheduleId}")
    public Result<String> submitRegister(
            @PathVariable Long patientId,
            @PathVariable Long scheduleId) {
        return registerService.submitRegister(patientId, scheduleId);
    }

    // 恢复取消挂号方法，去掉 Knife4j 注解
    @PostMapping("/cancel/{orderId}")
    public Result<String> cancelRegister(
            @PathVariable Long orderId) {
        return registerService.cancelRegister(orderId);
    }

    @GetMapping("/myOrder/{patientId}")
    public Result<List<RegisterOrder>> getMyOrder(
            @PathVariable Long patientId) {
        return registerService.getMyOrder(patientId);
    }

    @GetMapping("/queue/{patientId}")
    public Result<QueueStatus> getQueueStatus(@PathVariable Long patientId) {
        return registerService.getQueueStatus(patientId);
    }
}