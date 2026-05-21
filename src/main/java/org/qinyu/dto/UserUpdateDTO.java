package org.qinyu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户更新数据")
public class UserUpdateDTO {

    @Schema(description = "用户ID", example = "a1b2c3d4e5f67890abcdef1234")
    private String id;

    @Schema(description = "用户名", example = "qinyu")
    private String name;

    @Schema(description = "真实姓名", example = "")
    private String realName;

    @Schema(description = "邮箱", example = "123456@163.com")
    private String email;

    @Schema(description = "联系电话", example = "18782565458")
    private String phone;

}
