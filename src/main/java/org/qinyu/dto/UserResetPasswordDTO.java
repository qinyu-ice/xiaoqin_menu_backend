package org.qinyu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户重置密码数据")
public class UserResetPasswordDTO {

    @Schema(description = "用户ID", example = "a1b2c3d4e5f67890abcdef1234")
    private String id;

    @Schema(description = "密码", example = "$2a$10$Z7XQH5sK8aL9bJ2mN4vB5uC6iD7eF8gH9jK0lL1kK2jJ3hH4gG5fF")
    private String password;

    @Schema(description = "是否重置密码：1-未重置；2-重置中；3-已重置", example = "1")
    private Integer isResetPassword;

}
