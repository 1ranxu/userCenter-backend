package com.luoying.common;

import lombok.Data;


public enum ErrorCode {

    SUCCESS(1, "OK", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据空值", ""),
    NO_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    FORBIDDEN(403100,"禁止访问",""),
    SYSTEM_ERROR(50000, "系统内部异常", "");

    /**
     * 状态码信息
     */
    private final int code;
    /**
     * 状态码描述
     */
    private final String message;
    /**
     * 具体描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
