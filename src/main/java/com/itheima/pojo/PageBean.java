package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果封装类
 *
 * 知识点:
 *   泛型设计 —— 利用 List 接收任意类型数据行, 配合 PageHelper 插件使用
 *   Lombok 三件套 —— @Data + @NoArgsConstructor + @AllArgsConstructor
 *   PageHelper 分页后通过 PageInfo 获取 total 和 rows 再封装为 PageBean
 *   total + rows 是前端表格组件的标准分页数据结构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean {
    private Long total; // 总记录数 (用于前端计算总页数)
    private List rows;  // 当前页数据列表
}
