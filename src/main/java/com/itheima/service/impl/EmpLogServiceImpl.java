package com.itheima.service.impl;

import com.itheima.mapper.EmpLogMapper;
import com.itheima.pojo.EmpLog;
import com.itheima.service.EmpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 员工操作日志 Service 实现
 *
 * 知识点:
 *   @Service —— Spring IOC 容器管理, 自动注册为 Bean
 *   @Transactional(propagation = Propagation.REQUIRES_NEW) —— 事务传播行为
 *     REQUIRES_NEW: 无论外部是否有事务, 都开启一个新事务独立提交
 *     典型场景: 日志记录不受主业务事务回滚影响, 保证每次操作都有据可查
 */
@Service
public class EmpLogServiceImpl implements EmpLogService {

    @Autowired
    private EmpLogMapper empLogMapper;

    /**
     * 插入操作日志 (独立事务)
     * 知识点:
     *   REQUIRES_NEW 传播级别 —— 无论外部主业务是否存在事务, 都开启新事务独立提交
     *   效果: 即使员工新增/修改操作失败回滚, 此操作日志也会成功记录
     *   适用场景: 审计日志、通知记录等不能因业务失败而丢失的数据
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 无论是否有事务, 都要新建一个事务
    @Override
    public void insertLog(EmpLog empLog) {
        empLogMapper.insert(empLog);
    }

}
