package com.itheima.service.impl;

import com.itheima.mapper.LogMapper;
import com.itheima.pojo.Log;
import com.itheima.pojo.PageBean;
import com.itheima.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统操作日志 Service 实现
 *
 * 知识点:
 *   @Service —— Spring IOC 容器管理, 自动扫描注册为 Bean
 *   @Autowired —— 依赖注入 LogMapper
 *   手动分页 —— 先 COUNT 查询总记录数, 再 LIMIT 查询当前页数据, 组装为 PageBean
 *     与 PageHelper 插件对比: 手动分页更灵活, 适合简单场景
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    /**
     * 分页查询操作日志
     * 知识点:
     *   手动分页逻辑 (未使用 PageHelper 插件):
     *     1. logMapper.count() 查询总记录数
     *     2. 计算起始索引 start = (page-1) * pageSize
     *     3. logMapper.page(start, pageSize) 通过 SQL LIMIT 查询当前页数据
     *     4. 封装为 PageBean 返回给 Controller
     *   LIMIT start, pageSize —— MySQL 分页语法, start 从 0 开始
     */
    @Override
    public PageBean page(Integer page, Integer pageSize) {
        //1. 查询总记录数
        Long total = logMapper.count();
        //2. 计算起始索引 (第1页对应 start=0)
        Integer start = (page - 1) * pageSize;
        //3. 查询当前页数据
        List<Log> rows = logMapper.page(start, pageSize);
        //4. 封装分页结果
        return new PageBean(total, rows);
    }
}
