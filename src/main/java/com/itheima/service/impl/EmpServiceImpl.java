package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.*;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 员工管理 Service 实现
 *
 * 知识点:
 *   @Slf4j —— Lombok 日志注解, 编译时自动生成 log 对象, 简化日志代码
 *   @Service —— Spring IOC 容器管理, 默认单例模式
 *   @Transactional —— 事务管理, rollbackFor 指定哪些异常触发回滚
 *   事务传播 —— 通过注入 EmpLogService 实现日志独立事务 (REQUIRES_NEW)
 *   先删后增策略 —— 修改工作经历时, 先删除旧数据再批量新增, 避免逐条对比
 *   PageHelper —— MyBatis 分页插件, startPage 通过 ThreadLocal 设置分页参数
 */
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;           // 员工基本数据访问
    @Autowired
    private EmpExprMapper empExprMapper;   // 员工工作经历数据访问
    @Autowired
    private EmpLogService empLogService;   // 操作日志服务 (独立事务)

    /**
     * 分页条件查询员工列表
     * 知识点:
     *   PageHelper.startPage() —— 设置分页参数, 底层通过 ThreadLocal 传递, 线程安全
     *   查询结果强转为 Page 类型 —— Page 是 PageHelper 提供的 List 子类, 包含分页信息
     *   p.getTotal() / p.getResult() —— 获取总记录数和当前页数据
     */
    @Override
    public PageBean page(EmpQueryParam queryParam) {
        //1. 设置分页参数 - PageHelper
        PageHelper.startPage(queryParam.getPage(), queryParam.getPageSize());

        //2. 执行查询 (PageHelper 自动拦截, 追加 limit)
        List<Emp> empList = empMapper.list(queryParam);

        //3. 解析分页结果
        Page<Emp> p = (Page<Emp>) empList;
        return new PageBean(p.getTotal(), p.getResult());
    }

    /**
     * 新增员工 (含工作经历 + 操作日志)
     * 知识点:
     *   @Transactional(rollbackFor = {Exception.class}) —— 所有异常 (含受检异常) 都会回滚
     *   try-finally —— 无论主业务是否异常, finally 中的日志记录都会执行
     *   日志独立事务 —— EmpLogService.insertLog() 使用 REQUIRES_NEW 传播级别
     *     即使员工新增失败回滚, 操作日志也会独立提交保留
     *   主键回填 —— empMapper.insert() 后 emp.getId() 已获得数据库自增主键
     */
    @Transactional(rollbackFor = {Exception.class}) // 事务管理的注解 - 所有异常都会回滚
    @Override
    public void add(Emp emp) throws Exception {
        try {
            //1. 调用empMapper保存员工的基本信息
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp); // 执行后 emp.getId() 获得自增主键值 (主键回填)

            //2. 调用empExprMapper保存员工的工作经历信息
            List<EmpExpr> exprList = emp.getExprList();
            if (!CollectionUtils.isEmpty(exprList)) { // 判断集合是否为空/null
                // 设置每条工作经历的外键 (关联员工ID)
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                empExprMapper.insertBatch(exprList); // 批量保存工作经历
            }
        } finally {
            // 无论主事务是否成功, 都记录操作日志 (日志使用独立事务)
            try {
                EmpLog empLog = new EmpLog(null, LocalDateTime.now(), emp.toString());
                empLogService.insertLog(empLog);
            } catch (Exception e) {
                log.error("新增员工操作日志失败: {}", e.getMessage());
            }
        }
    }

    /**
     * 批量删除员工及其工作经历
     * 知识点: 两表删除 —— 先删除员工基本信息, 再删除关联工作经历, 保证数据一致性
     *   注意顺序: 实际场景中应先删除子表 (工作经历), 再删除主表 (员工), 避免外键约束冲突
     */
    @Override
    public void deleteById(List<Integer> ids) {
        //批量删除员工基本信息
        empMapper.deleteByIds(ids);

        //批量删除关联工作经历
        empExprMapper.deleteByEmpIds(ids);
    }

    /**
     * 根据ID查询员工详情
     * 知识点: Mapper 联表查询 —— 员工基本信息 + 工作经历列表组合为 Emp 对象返回
     *   ResultMap 的 collection 标签自动将多条工作记录映射到 List<EmpExpr>
     */
    @Override
    public Emp getInfo(Integer id) {
        //返回查询封装好的完整数据 (含工作经历列表)
        return empMapper.getById(id);
    }

    /**
     * 修改员工信息 (先删后增策略)
     * 知识点:
     *   先删后增 —— 先删除所有旧工作经历, 再批量插入新数据
     *     避免了逐条对比新旧数据的复杂度, 实现简单清晰
     *   事务保证 —— 删除和新增在同一事务中, 要么全部成功要么全部回滚
     *   Arrays.asList() —— 将单个 ID 转为 List, 复用批量删除方法
     */
    @Override
    public void update(Emp emp) {
        //1. 根据id更新员工基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        //2. 根据员工ID删除原有的工作经历信息 【删除老的】
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));

        //3. 新增员工的工作经历数据 【新增新的】
        Integer empId = emp.getId();
        List<EmpExpr> exprList = emp.getExprList();
        // 循环设置外键
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> {
                empExpr.setEmpId(empId);
            });

            //添加新的工作经历
            empExprMapper.insertBatch(exprList);
        }
    }

    /**
     * 查询所有员工 (仅基本信息, 不含工作经历)
     * 知识点: 返回简单员工列表, 适用于前端下拉选择框等场景
     */
    @Override
    public List<Emp> findAll() {
        return empMapper.findAll();
    }

    /**
     * 根据用户名查询员工 (登录认证用)
     * 知识点: 用于登录校验 —— 查询用户信息后在 Controller 层进行密码比对
     */
    @Override
    public Emp getByUsername(String username) {
        return empMapper.getByUsername(username);
    }

    /**
     * 修改员工密码
     * 知识点: 调用 Mapper 执行 update password 语句
     *   Controller 层需校验旧密码正确性, Service 层只负责更新
     */
    @Override
    public void changePassword(Emp emp) {
        empMapper.updatePassword(emp);
    }
}
