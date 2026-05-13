package com.itheima.exception;

import com.itheima.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * 知识点:
 *   @RestControllerAdvice —— Spring MVC 全局异常处理注解
 *     相当于 @ControllerAdvice + @ResponseBody, 拦截所有 @RequestMapping 方法抛出的异常
 *     将返回值直接序列化为 JSON 响应体
 *   @ExceptionHandler —— 标注在方法上, 声明该方法处理指定类型的异常
 *     此处参数为 Exception.class, 表示捕获所有异常类型
 *   统一响应格式 —— 所有异常均封装为 Result 对象返回, 保证前后端接口约定的一致性
 *
 * 作用: 避免将异常堆栈直接暴露给前端, 提升系统的安全性和用户体验
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 通用的异常处理方法
     *
     * 拦截所有控制器中未处理的 Exception, 将异常信息提取后封装为统一的 Result 响应
     * 在实际项目中可根据需要细分异常类型 (如 NullPointerException、业务异常等)
     * 通过 @ExceptionHandler(具体异常.class) 实现差异化处理
     *
     * @param ex 捕获到的异常对象
     * @return 统一封装的错误响应 Result, 其中 msg 为异常的描述信息
     */
    @ExceptionHandler // 不指定异常类型则默认处理所有异常
    public Result exceptionHandler(Exception ex) {
        // 返回统一错误格式: {code: 0, msg: "异常信息"}
        return Result.error(ex.getMessage());
    }
}
