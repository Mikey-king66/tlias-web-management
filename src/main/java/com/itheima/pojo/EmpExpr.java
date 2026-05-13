package com.itheima.pojo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 员工工作经历实体类
 *
 * 知识点:
 *   与 Emp 实体为一对多关系 —— 一个员工有多段工作经历
 *   empId 为外键, 关联 emp 表的主键 id
 *   MyBatis 中通过 ResultMap 的 &lt;collection&gt; 标签将多条 Expr 映射到 List&lt;EmpExpr&gt;
 *   修改员工信息时采用"先删后增"策略保证关联数据一致性
 */
@Data
public class EmpExpr {
    private Integer id;    // ID, 主键
    private Integer empId; // 员工ID (外键, 关联 emp 表)
    private LocalDate begin; // 开始时间
    private LocalDate end;   // 结束时间
    private String company;  // 公司名称
    private String job;      // 职位名称
}
