package com.example.hospital.controller;

import com.example.hospital.common.Result;
import com.example.hospital.service.AIService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Resource
    private AIService aiService;

    @PostMapping("/ask")
    public Result<String> askQuestion(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        return aiService.askQuestion(question);
    }

    @GetMapping("/departments")
    public Result<List<Map<String, Object>>> getAllDepartments() {
        return aiService.getAllDepartments();
    }
}