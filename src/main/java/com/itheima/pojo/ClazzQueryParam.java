package com.itheima.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 班级查询参数封装类
 *
 * 知识点:
 *   @DateTimeFormat —— Spring MVC 日期格式绑定, 将请求参数中的日期字符串 (yyyy-MM-dd) 转为 LocalDate
 *   前端传递的 queryString 参数会自动绑定到同名属性上
 *   分页参数设置默认值, 避免 Controller 层重复处理
 */
@Data
public class ClazzQueryParam {

    private String name; // 班级名称 (模糊查询)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin; // 结课时间范围开始
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;   // 结课时间范围结束

    private Integer page = 1;      // 当前页码, 默认第1页
    private Integer pageSize = 10; // 每页记录数, 默认10条
}
