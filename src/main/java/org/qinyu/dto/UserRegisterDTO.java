package org.qinyu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户注册数据")
public class UserRegisterDTO {

    @Schema(description = "用户名", example = "qinyu")
    private String name;

    @Schema(description = "密码", example = "123456")
    private String password;

    @Schema(description = "二次密码", example = "123456")
    private String rePassword;
}
