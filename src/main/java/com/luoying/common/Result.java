package com.luoying.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用结果返回类
 *
 * @param
 * @author 落樱的悔恨
 */
@Data
public class Result implements Serializable {
    private int code;

    private Object data;

    private String message;

    private String description;

    public Result(int code, Object data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static Result success(Object data) {
        return new Result(1, data, "ok", "");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static Result error(ErrorCode errorCode) {
        return new Result(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public static Result error(ErrorCode errorCode, String description) {
        return new Result(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    public static Result error(ErrorCode errorCode, String message, String description) {
        return new Result(errorCode.getCode(), null, message, description);
    }

    public static Result error(int code, Object data, String message, String description) {
        return new Result(code, data, message, description);
    }
}
