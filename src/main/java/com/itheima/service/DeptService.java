package com.itheima.service;

import com.itheima.pojo.Dept;

import java.util.List;

/**
 * 部门管理 Service 接口
 *
 * 知识点:
 *   面向接口编程 —— Controller 通过接口调用, 不依赖具体实现, 降低耦合
 *   业务分层架构 —— Service 层封装业务逻辑, Mapper 层负责数据访问
 *   后续如需切换实现 (如引入 MyBatis-Plus), 只需替换实现类, Controller 无需改动
 */
public interface DeptService {

    /**
     * 查询部门列表
     * 知识点: 直接调用 Mapper 查询全部, Service 层可在此扩展缓存逻辑 (如 Redis 缓存)
     */
    public List<Dept> list() throws Exception;

    /**
     * 根据ID删除部门
     * 知识点: 删除操作需检查关联数据, 如有员工关联需级联处理或禁止删除
     */
    void delete(Integer id);

    /**
     * 添加部门
     * 知识点: 参数 Dept 由前端 JSON 自动绑定 (@RequestBody), Service 层补全 createTime/updateTime
     */
    void add(Dept dept);

    /**
     * 根据ID查询部门
     * 知识点: 查询单个对象, 可用于修改表单的数据回显
     */
    Dept getById(Integer id);

    /**
     * 修改部门
     * 知识点: 更新前补全 updateTime, 结合动态 SQL 只更新非空字段
     */
    void update(Dept dept);
}
