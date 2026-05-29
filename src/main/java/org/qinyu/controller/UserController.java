package org.qinyu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.qinyu.dto.UserLoginDTO;
import org.qinyu.dto.UserRegisterDTO;
import org.qinyu.dto.UserResetPasswordDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;
import org.qinyu.service.UserService;
import org.qinyu.util.AliOssUtil;
import org.qinyu.util.JwtUtil;
import org.qinyu.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户服务", description = "用户注册登录，增删改查")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AliOssUtil aliOssUtil;

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
    @Operation(summary = "用户申请重置密码", description = "根据用户名将用户密码重置字段修改为重置中状态")
    public Result<String> applyResetPassword(@RequestParam String name) {
        return Result.ok(userService.applyResetPassword(name));
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

    @GetMapping("/status")
    @Operation(summary = "获取当前用户状态", description = "获取token的状态")
    public Result<Boolean> status(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = getToken(authHeader);
        return Result.ok("获取用户状态成功", jwtUtil.validateToken(token));
    }

    @GetMapping("/exit")
    @Operation(summary = "用户登出", description = "删除token等信息")
    public Result<Boolean> exit(@RequestParam String id) {
        return Result.ok("用户登出成功", userService.exit(id));
    }

    @PostMapping("upload/avatar")
    @Operation(summary = "上传头像", description = "上传本地头像到阿里云OSS对象存储")
    public Result<String> uploadAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.no("文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String objectName = "user/" + UUID.randomUUID() + suffix;

        try {
            byte[] bytes = file.getBytes();
            String url = aliOssUtil.upload(bytes, objectName);
            return Result.ok("文件上传成功", url);
        } catch (IOException e) {
            log.error("读取文件失败", e);
            return Result.no("文件读取失败");
        } catch (RuntimeException e) {
            log.error("OSS上传失败", e);
            return Result.no("上传失败: " + e.getMessage());
        }
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
