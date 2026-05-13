package com.itheima.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 员工查询参数封装类 (DTO 模式)
 *
 * 知识点:
 *   DTO (Data Transfer Object) —— 将多个查询条件封装为一个对象, 简化 Controller 方法签名
 *   @DateTimeFormat —— Spring MVC 参数绑定, 自动将请求中的日期字符串转为 LocalDate
 *   分页参数默认值 —— page=1, pageSize=10, 防止空值异常
 *   begin/end 组成日期范围查询条件, 配合 MyBatis 动态 SQL 的 &lt;if&gt; 标签实现非空过滤
 */
@Data
public class EmpQueryParam {

    private String name;   // 员工姓名 (模糊查询)
    private Integer gender; // 性别: 1=男, 2=女 (精确匹配)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin; // 入职日期范围开始
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;   // 入职日期范围结束

    private Integer page = 1;      // 当前页码, 默认第1页
    private Integer pageSize = 10; // 每页记录数, 默认10条

}
