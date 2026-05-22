package org.qinyu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("chat_user")
public class ChatUserBind {

    @Schema(description = "用户ID", example = "a1b2c3d4e5f67890abcdef1234")
    private String userId;

    @TableId
    @Schema(description = "会话ID", example = "ceshi01")
    private String conversationId;
}
