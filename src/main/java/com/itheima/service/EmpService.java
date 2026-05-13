package com.itheima.service;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageBean;

import java.util.List;

/**
 * 员工管理 Service 接口
 *
 * 知识点:
 *   面向接口编程 —— 定义员工业务规范, 实现类可灵活替换
 *   业务分层架构 —— Service 封装员工基本信息 + 工作经历组合业务
 *   事务管理 —— 新增/修改涉及多表操作, 由实现层通过 @Transactional 保证原子性
 */
public interface EmpService {

    /**
     * 分页条件查询
     */
    PageBean page(EmpQueryParam queryParam);

    /**
     * 新增员工 (含工作经历)
     * 知识点: 事务管理 —— 员工基本信息 + 工作经历需在同一事务中插入, 保证数据一致性
     */
    void add(Emp emp) throws Exception;

    /**
     * 批量删除员工 (含工作经历)
     * 知识点: 事务管理 —— 删除员工基本信息和工作经历需在同一事务中保证原子性
     */
    void deleteById(List<Integer> ids);

    /**
     * 根据ID查询员工详情 (含工作经历)
     * 知识点: 联表查询 —— 员工基本信息 + 工作经历列表组合返回
     */
    Emp getInfo(Integer id);

    /**
     * 修改员工信息 (含工作经历)
     * 知识点: 先删后增策略 —— 先删除旧工作经历再批量新增, 避免逐条比较新旧数据
     */
    void update(Emp emp);

    /**
     * 查询所有员工
     * 知识点: 用于下拉选择或导出等场景, 不含工作经历等复杂关联
     */
    List<Emp> findAll();

    /**
     * 根据用户名查询员工 (登录验证)
     * 知识点: 用于登录校验 —— 根据用户名查询用户信息, 在 Controller 层比对密码
     */
    Emp getByUsername(String username);

    /**
     * 修改密码
     * 知识点: 密码修改操作, Controller 层需先校验旧密码正确性
     */
    void changePassword(Emp emp);
}
