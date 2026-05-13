package com.itheima.service;

import com.itheima.pojo.EmpLog;

/**
 * 员工操作日志 Service 接口
 *
 * 知识点:
 *   面向接口编程 —— 定义操作日志写入规范
 *   事务传播行为 —— 实现类中使用 REQUIRES_NEW 保证日志记录独立于主事务
 */
public interface EmpLogService {

    /**
     * 插入员工操作日志
     * 知识点: 无论主业务事务是否回滚, 日志都需要独立提交
     * @param empLog 员工操作日志对象
     */
    public void insertLog(EmpLog empLog);

}
