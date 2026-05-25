package org.qinyu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.qinyu.dto.UserLoginDTO;
import org.qinyu.dto.UserRegisterDTO;
import org.qinyu.dto.UserResetPasswordDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;
import org.qinyu.service.UserService;
import org.qinyu.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    @Operation(summary = "更新用户信息", description = "根据用户ID更新用户信息：修改密码、启用/禁用用户")
    public Result<String> update(@RequestBody UserUpdateDTO dto, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = getToken(authHeader);
        return Result.ok(userService.update(dto, token));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户", description = "根据用户ID删除指定用户")
    public Result<String> delete(@RequestBody List<String> ids) {
        return Result.ok(userService.delete(ids));
    }

    @PostMapping("/apply/reset/password")
    @Operation(summary = "用户申请重置密码", description = "根据用户ID将用户密码重置字段修改为重置中状态")
    public Result<String> applyResetPassword(@RequestParam String id) {
        return Result.ok(userService.applyResetPassword(id));
    }

    @PostMapping("/reset/password")
    @Operation(summary = "用户重置密码", description = "根据用户ID将用户密码重置")
    public Result<String> resetPassword(@RequestBody List<UserResetPasswordDTO> dtoList) {
        return Result.ok(userService.resetPassword(dtoList));
    }

    @GetMapping("/info/{id}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    public Result<User> getUserInfo(@PathVariable String id, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = getToken(authHeader);
        return Result.ok("获取用户信息成功", userService.getUserInfo(id, token));
    }

    @PostMapping("/edit/permission")
    @Operation(summary = "修改用户权限", description = "根据用户ID修改用户权限")
    public Result<String> editPermission(@RequestParam String id, @RequestParam Integer permission, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = getToken(authHeader);
        return Result.ok(userService.editPermission(id, permission, token));
    }

    // 获取 token
    private String getToken(String authHeader) {
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        return token;
    }
}
