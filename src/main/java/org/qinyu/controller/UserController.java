package org.qinyu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.qinyu.dto.UserLoginDTO;
import org.qinyu.dto.UserRegisterDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.service.UserService;
import org.qinyu.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name = "用户服务", description = "用户注册登录，增删改查") // 核心注释
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "创建用户")
    public Result<String> register(@RequestBody UserRegisterDTO dto) {
        return Result.ok(userService.add(dto));
    }

    @PostMapping("login")
    @Operation(summary = "用户登录", description = "校验用户名是否存在，密码是否正确")
    public Result<Map<String, Object>> login(@RequestBody UserLoginDTO dto) {
        Map<String, Object> map = userService.login(dto);
        String message = (String) map.get("message");
        Map<String, Object> data = new HashMap<>(map);
        data.remove("message");
        return Result.ok(message, data);
    }

    @PostMapping("/update")
    @Operation(summary = "更新用户信息", description = "根据用户ID更新用户信息")
    public Result<String> update(@RequestBody UserUpdateDTO dto) {
        return Result.ok(userService.update(dto));
    }
}
