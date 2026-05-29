package org.qinyu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_collection_dish")
@Schema(description = "用户收藏菜品数据")
public class UserCollectionDish {

    @Schema(description = "用户ID", example = "a1b2c3d4e5f67890abcdef1234")
    private String userId;

    @Schema(description = "菜品ID", example = "dishc3d4e5f67890abcdef1234")
    private String dishId;

    @Schema(description = "收藏时间", example = "")
    private LocalDateTime time;
}
