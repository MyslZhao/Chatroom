package com.example.demo.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    /** 业务错误码，默认 500 */
    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}