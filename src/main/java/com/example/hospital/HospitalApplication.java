package com.example.hospital;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 实现 WebMvcConfigurer 接口，配置视图重定向
@SpringBootApplication
@MapperScan("com.example.hospital.mapper")
public class HospitalApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
        System.out.println("✅ 医院挂号系统启动成功！");
        System.out.println("🔗 访问地址：http://localhost:8081/");
    }

    // 重写 addViewControllers 方法，配置根路径重定向
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 访问根路径 / 时，直接转发到 index.html（地址栏不变，体验更好）
        registry.addViewController("/").setViewName("forward:/login.html");
    }
}