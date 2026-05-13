package com.itheima.service;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.PageBean;

import java.util.List;

/**
 * 班级管理 Service 接口
 *
 * 知识点:
 *   面向接口编程 —— 定义规范解耦, Controller 只依赖接口不依赖实现类
 *   后续如需切换实现 (如引入 MyBatis-Plus), 只需替换 Impl 即可
 */
public interface ClazzService {

    /**
     * 条件分页查询班级
     * 知识点: 参数封装为 QueryParam 对象, 避免方法签名过长
     */
    PageBean page(ClazzQueryParam queryParam);

    /**
     * 新增班级信息
     * 知识点: 参数为 Clazz 实体, 前端 JSON 经 @RequestBody 自动转换传入
     */
    void save(Clazz clazz);

    /**
     * 根据ID查询班级 (含班主任姓名联表查询 + 状态计算)
     */
    Clazz getById(Integer id);

    /**
     * 修改班级信息
     * 知识点: 动态更新 —— 仅更新前端传递的非空字段 (XML set/if 实现)
     */
    void update(Clazz clazz);

    /**
     * 根据ID删除班级
     * 知识点: 业务规则校验 —— 关联有学员时禁止删除, 抛出 RuntimeException 由全局异常处理器统一返回错误
     */
    void delete(Integer id);

    /**
     * 查询全部班级 (用于新增学员时选择归属班级的下拉列表)
     */
    List<Clazz> findAll();
}
