package com.itheima.service;

import com.itheima.pojo.PageBean;

/**
 * 系统操作日志 Service 接口
 *
 * 知识点:
 *   面向接口编程 —— 定义日志查询规范
 *   分页查询 —— 参数 page/pageSize 控制分页, PageBean 封装结果
 */
public interface LogService {

    /**
     * 分页查询操作日志
     * 知识点: 手动分页 —— 计算 limit 起始索引 (start = (page-1) * pageSize)
     * @param page 当前页码
     * @param pageSize 每页条数
     * @return 分页结果封装对象
     */
    PageBean page(Integer page, Integer pageSize);
}
