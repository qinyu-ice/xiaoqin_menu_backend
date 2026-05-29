package org.qinyu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.qinyu.entity.Dish;
import org.qinyu.service.DishService;
import org.qinyu.util.AliOssUtil;
import org.qinyu.util.Result;
import org.qinyu.vo.PageVO;
import org.qinyu.vo.SimpleDishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/dish")
@Tag(name = "菜品服务", description = "菜品增删改查，收藏")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/add")
    @Operation(summary = "新增菜品", description = "新增菜品")
    public Result<String> addDish(@RequestBody Dish dish, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = getToken(authHeader);
        return Result.ok(dishService.add(dish, token));
    }

    @GetMapping("/{page}/{pageSize}")
    @Operation(summary = "分页查询菜品", description = "")
    public Result<PageVO<SimpleDishVO>> findByPage(@PathVariable Integer page, @PathVariable Integer pageSize,
                                                   @RequestParam(required = false, defaultValue = "") String name) {
        Page<Dish> paged = dishService.getByPage(page, pageSize, name);
        if (paged.getRecords().isEmpty()) {
            return Result.ok("暂无相关菜品", new PageVO<>(paged.getTotal(), paged.getRecords().stream().map(SimpleDishVO::new).toList()));
        }
        return Result.ok("成功获取第" + page + "页菜品", new PageVO<>(paged.getTotal(),
                paged.getRecords().stream().map(SimpleDishVO::new).toList()));
    }

    @PostMapping("upload")
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
        String objectName = "dish/" + UUID.randomUUID() + suffix;

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
