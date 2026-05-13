package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 员工操作日志实体类
 *
 * 知识点:
 *   审计日志设计 —— 记录什么时间做了什么操作
 *   operateTime 记录操作时间点, 用于日志追踪
 *   info 字段存储操作摘要信息 (如 "新增员工-张三")
 *   日志写入使用独立事务 (REQUIRES_NEW), 即使业务操作回滚日志也会被保留
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpLog {
    private Integer id; // ID, 主键
    private LocalDateTime operateTime; // 操作时间
    private String info; // 详细信息 (操作摘要)
}
