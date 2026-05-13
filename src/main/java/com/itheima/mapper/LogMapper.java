package com.itheima.mapper;

import com.itheima.pojo.Log;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统日志 Mapper 接口 (操作日志管理)
 *
 * 知识点:
 *   @Mapper —— MyBatis 映射器注解, 由 MyBatis 动态代理实现并注入 Spring 容器
 *   传统分页 —— 使用 limit #{start}, #{pageSize} 实现手动分页查询
 *     (与 PageHelper 插件对比: 适用于简单场景, 无需额外依赖)
 *   XML 映射 —— 所有 SQL 在 LogMapper.xml 中定义, namespace 全限定名关联接口
 */
@Mapper
public interface LogMapper {

    /**
     * 分页查询日志列表 (XML 实现, 传统 limit 分页)
     * 知识点: 传统分页方式 —— 通过 limit #{start}, #{pageSize} 实现偏移量分页
     *   适用场景: 数据量较小时可直接使用, 数据量大时推荐使用 PageHelper
     */
    List<Log> page(Integer start, Integer pageSize);

    /**
     * 查询日志总记录数 (XML 实现)
     * 知识点: COUNT(*) 聚合函数统计总条数, 用于前端分页组件计算总页数
     */
    Long count();
}
