package com.example.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.hospital.common.Result;
import com.example.hospital.entity.SysUser;
import com.example.hospital.mapper.SysUserMapper;
import com.example.hospital.entity.Login;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private SysUserMapper userMapper;

    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody Login loginDTO) {
        if (loginDTO == null || loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
            return Result.fail("用户名或密码不能为空");
        }

        // 1. 查询用户是否存在
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, loginDTO.getUsername()));

        if (user == null) {
            return Result.fail("用户不存在");
        }

        // 2. 验证密码（期末作业使用明文比对即可）
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            return Result.fail("密码错误");
        }

        // 3. 检查账号是否被禁用
        if (user.getStatus() != null && user.getStatus() == 0) {
            return Result.fail("账号已被禁用，请联系管理员");
        }

        // 4. 登录成功，将用户信息（包含 id、真实姓名、角色等）返回给前端
        return Result.success(user);
    }
}