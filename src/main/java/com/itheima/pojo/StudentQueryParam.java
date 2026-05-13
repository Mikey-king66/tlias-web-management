package com.itheima.pojo;

import lombok.Data;

/**
 * 学员查询参数封装类
 *
 * 知识点:
 *   将多个请求参数封装为一个对象, 简化 Controller 方法签名
 *   Spring MVC 自动将同名 queryString 参数绑定到对象属性
 */
@Data
public class StudentQueryParam {

    private String name;      // 学员姓名 (模糊查询)
    private Integer degree;   // 学历 (精确匹配, 1:初中 ~ 6:博士)
    private Integer clazzId;  // 班级ID (精确匹配)

    private Integer page = 1;      // 当前页码, 默认第1页
    private Integer pageSize = 10; // 每页记录数, 默认10条
}
