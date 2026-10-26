package com.example.demo.exception;

import com.example.demo.utils.Result;
import com.example.demo.utils.ResultUntils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHander {

    /** 客户端主动断开连接（如取消视频加载、关闭页面等），无需处理 */
    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbortException(ClientAbortException e) {
        log.warn("客户端断开连接: {}", e.getMessage());
    }

    /** 业务异常：用户名或密码错误、账号被封禁等 */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResultUntils.fail(e.getCode(), e.getMessage());
    }

    /** @Valid 校验失败（@RequestBody 参数校验） */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        return ResultUntils.fail(400, message);
    }

    /** 兜底：其他未捕获的异常 */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return ResultUntils.fail("系统繁忙，请稍后重试");
    }
}