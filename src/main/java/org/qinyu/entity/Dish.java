package org.qinyu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dish")
@Schema(description = "菜品数据")
public class Dish {

    @Schema(description = "菜品ID", example = "dishc3d4e5f67890abcdef1234")
    private String id;

    @Schema(description = "名称", example = "鱼香肉丝")
    private String name;

    @Schema(description = "简介", example = "鱼香肉丝是一道经典的川菜，其特点是咸、甜、酸、辣、香五味俱全，而且不含鱼。")
    private String introduction;

    @Schema(description = "图片", example = "")
    private String img;

    @Schema(description = "类别", example = "炒菜")
    private String kind;

    @Schema(description = "派系", example = "川菜")
    private String faction;

    @Schema(description = "调味品", example = "酱油，醋")
    private String seasoning;

    @Schema(description = "食材", example = "猪肉，胡罗卜")
    private String foodMaterial;

    @Schema(description = "制作步骤", example = "1.先腌制猪肉")
    private String procedureStep;

    @Schema(description = "注意事项", example = "肉丝要切得细一些，这样更容易入味。")
    private String note;

    @Schema(description = "作者", example = "qinyu")
    private String author;

    @Schema(description = "创建时间", example = "")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "")
    private LocalDateTime updateTime;
}
