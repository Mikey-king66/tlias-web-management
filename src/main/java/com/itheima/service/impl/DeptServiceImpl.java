package com.itheima.service.impl;

import com.itheima.mapper.DeptMapper;
import com.itheima.pojo.Dept;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门管理 Service 实现
 *
 * 知识点:
 *   @Service —— Spring 业务层组件, IOC 容器管理, 默认单例 (scope=singleton)
 *   @Autowired —— 依赖注入 DeptMapper, 由容器自动装配 (按类型注入)
 *   DAO 封装 —— Service 调用 Mapper 完成数据持久化, 各方法职责单一
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper; // 部门数据访问层

    /**
     * 查询部门列表
     * 知识点: 直接委托 Mapper 查询, Service 层可在此扩展缓存/权限过滤等逻辑
     */
    @Override
    public List<Dept> list() throws Exception {
        return deptMapper.findAll();
    }

    /**
     * 根据ID删除部门
     * 知识点: 当前为物理删除, 可扩展为软删除 (update deleted_flag=1)
     */
    @Override
    public void delete(Integer id) {
        deptMapper.deleteById(id);
    }

    /**
     * 新增部门
     * 知识点: 补全 createTime 和 updateTime 后再持久化, 保证审计字段有值
     *   LocalDateTime.now() —— Java 8 获取当前日期时间, 线程安全
     */
    @Override
    public void add(Dept dept) {
        //1. 为基础属性赋值
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        //2. 调用mapper接口
        deptMapper.insert(dept);
    }

    /**
     * 根据ID查询部门
     * 知识点: 查询单个部门, 用于修改页面的数据回显
     */
    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }

    /**
     * 修改部门信息
     * 知识点: 补全 updateTime 后, Mapper 使用动态 SQL (set/if标签) 只更新非空字段
     *   动态更新优势: 前端未传的字段不会被 null 覆盖数据库原有数据
     */
    @Override
    public void update(Dept dept) {
        //1. 补全基础属性
        dept.setUpdateTime(LocalDateTime.now());
        //2. 更新数据
        deptMapper.update(dept);
    }
}
