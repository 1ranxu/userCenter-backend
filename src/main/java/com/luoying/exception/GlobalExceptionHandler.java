package com.luoying.exception;

import com.luoying.common.ErrorCode;
import com.luoying.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author 落樱的悔恨
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.error("BusinessException:{}", e.getMessage(), e);
        return Result.error(e.getCode(), null, e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException:{}", e);
        return Result.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
