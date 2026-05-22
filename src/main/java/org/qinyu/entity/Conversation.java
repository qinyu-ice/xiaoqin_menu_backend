package org.qinyu.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("spring_ai_chat_memory")
public class Conversation {

    @TableId
    @Schema(description = "会话ID", example = "ceshi01")
    private String conversationId;

    @Schema(description = "会话内容", example = "你好")
    private String content;

    @Schema(description = "会话人", example = "USER")
    private String type;

    @Schema(description = "会话时间", example = "1779431799000")
    private LocalDateTime timestamp;
}
