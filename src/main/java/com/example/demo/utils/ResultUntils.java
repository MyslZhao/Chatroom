package com.example.demo.utils;

import org.springframework.stereotype.Component;

@Component
public class ResultUntils {

    public static final int SUCCESS_CODE = 200;

    /** 失败响应码 */
    public static final int FAIL_CODE = 500;

    /** 成功，无数据 */
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS_CODE, "success", null);
    }

    /** 成功，带数据 */
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, "success", data);
    }

    /** 成功，自定义消息 + 数据 */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS_CODE, message, data);
    }

    /** 失败，默认码 500 */
    public static <T> Result<T> fail(String message) {
        return new Result<>(FAIL_CODE, message, null);
    }

    /** 失败，自定义码 */
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }
}