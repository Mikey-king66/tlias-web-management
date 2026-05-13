package com.itheima.pojo;

import lombok.Data;

/**
 * 后端统一返回结果封装类
 *
 * 知识点:
 *   @Data —— Lombok 注解, 编译时自动生成 getter/setter/toString/equals/hashCode
 *   静态工厂方法模式 —— 通过 success()/error() 静态方法创建实例, 统一响应格式
 *   泛型支持 —— data 字段为 Object 类型, 可接收任意类型数据 (List、Map、POJO 等)
 *   前后端分离规范 —— 统一的 {code, msg, data} JSON 响应结构
 */
@Data
public class Result {

    private Integer code; // 状态码: 1表示成功, 0表示失败
    private String msg;   // 提示信息 (成功或错误消息)
    private Object data;  // 响应数据 (可为 List、Map、POJO 或 null)

    /**
     * 返回成功响应 (无数据)
     * 知识点: 静态工厂方法 —— 无需 new 对象, 直接 Result.success() 调用, 代码更简洁
     */
    public static Result success() {
        Result result = new Result();
        result.code = 1;
        return result;
    }

    /**
     * 返回成功响应 (带数据)
     * @param object 响应数据, 支持任意类型
     */
    public static Result success(Object object) {
        Result result = new Result();
        result.data = object;
        result.code = 1;
        return result;
    }

    /**
     * 返回错误响应
     * @param msg 错误提示信息, 由全局异常处理器或业务逻辑传入
     */
    public static Result error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
