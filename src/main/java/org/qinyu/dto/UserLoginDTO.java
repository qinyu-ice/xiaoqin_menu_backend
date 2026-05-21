package org.qinyu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户登录数据")
public class UserLoginDTO {

    @Schema(description = "用户名", example = "qinyu")
    private String name;

    @Schema(description = "密码", example = "123456")
    private String password;
}
