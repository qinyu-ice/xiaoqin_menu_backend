package org.qinyu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户更新数据")
public class UserUpdateDTO {

    @Schema(description = "用户ID", example = "a1b2c3d4e5f67890abcdef1234")
    private String id;

    @Schema(description = "昵称", example = "qin")
    private String nickname;

    @Schema(description = "密码", example = "$2a$10$Z7XQH5sK8aL9bJ2mN4vB5uC6iD7eF8gH9jK0lL1kK2jJ3hH4gG5fF")
    private String password;

    @Schema(description = "真实姓名", example = "")
    private String realName;

    @Schema(description = "邮箱", example = "123456@163.com")
    private String email;

    @Schema(description = "联系电话", example = "18782565458")
    private String phone;

    @Schema(description = "是否启用：1-启用；0-禁用", example = "1")
    private Integer isEnable;

}
