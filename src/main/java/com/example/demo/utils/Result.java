package com.example.demo.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 响应码：200 成功，其他为失败 */
    private int code;

    /** 响应消息 */
    private String message;

    /** 响应数据 */
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}