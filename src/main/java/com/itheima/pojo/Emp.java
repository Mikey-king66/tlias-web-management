package com.itheima.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工实体类
 *
 * 知识点:
 *   Lombok @Data —— 编译时自动生成 getter/setter/toString/equals/hashCode
 *   Java 8 时间 API —— LocalDate (日期)、LocalDateTime (日期时间), 线程安全不可变
 *   包装类型 vs 基本类型 —— 推荐用包装类型, null 表示"未设置"
 *   deptName 和 exprList 为非数据库字段:
 *     - deptName: 通过 MyBatis 左连接 dept 表查询获得
 *     - exprList: 通过 MyBatis ResultMap 的 collection 标签映射 (一对多)
 *   job 字段使用数字编码, 通过 SQL 中的 CASE WHEN 转换为中文显示
 */
@Data
public class Emp {
    private Integer id;       // ID, 主键
    private String username;   // 用户名 (登录用)
    private String password;   // 密码
    private String name;       // 姓名
    private Integer gender;    // 性别, 1:男, 2:女
    private String phone;      // 手机号
    private Integer job;       // 职位, 1:班主任, 2:讲师, 3:学工主管, 4:教研主管, 5:咨询师
    private Integer salary;    // 薪资
    private String image;      // 头像 (OSS 文件 URL)
    private LocalDate entryDate;    // 入职日期
    private Integer deptId;         // 关联的部门ID (外键)
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 修改时间

    // 以下字段为非数据库字段, 通过关联查询映射填充
    private String deptName;  // 部门名称 (联表查询 dept 表)
    private List<EmpExpr> exprList; // 工作经历列表 (ResultMap collection 一对多映射)
}
