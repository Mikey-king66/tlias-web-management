package com.itheima.exception;

import com.itheima.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description GlobalExceptionHandler
 * @Author Mikey
 * @Date 2026-05-12  17:45
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //异常处理
    @ExceptionHandler
    public Result exceptionHandler(Exception ex){
        return Result.error(ex.getMessage());
    }
}
