package org.qinyu.util;

import lombok.Getter;

@Getter
public enum Code {
    OK(200, "请求资源成功"),
    NO(400, "请求资源失败"),
    CREATED(201, "创建资源成功"),
    UPDATED(204, "更新资源成功"),
    UNAUTHORIZED(401, "缺失身份验证凭证"),
    FORBIDDEN(403, "权限不足"),
    ERROR(500, "服务器内部错误"),
    ;

    private final Integer code;
    private final String desc;

    Code(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
