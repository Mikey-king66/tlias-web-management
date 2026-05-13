package com.itheima.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志实体类 (AOP 自动记录)
 *
 * 知识点:
 *   AOP 切面编程 —— 通过自定义注解 + 切面类自动捕获方法调用信息并写入日志表
 *   记录内容 —— 操作人、操作时间、类名、方法名、耗时、参数、返回值
 *   与 EmpLog 的区别 —— Log 记录 Controller 层 API 调用日志, EmpLog 记录员工表的增删改操作日志
 *   costTime 可配合 System.currentTimeMillis() 计算方法执行耗时
 */
@Data
public class Log {
    private Integer id;              // ID, 主键
    private String operateEmpName;   // 操作人姓名
    private LocalDateTime operateTime; // 操作时间
    private String className;        // 操作类名
    private String methodName;       // 操作方法名
    private Integer costTime;        // 方法执行耗时 (毫秒)
    private String methodParams;     // 方法请求参数
    private String returnValue;      // 方法返回值
}
