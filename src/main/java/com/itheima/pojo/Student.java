package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学员实体类
 *
 * 知识点:
 *   Lombok @Data —— 编译时自动生成 getter/setter/toString/equals/hashCode
 *   @NoArgsConstructor —— 生成无参构造 (反序列化需要)
 *   @AllArgsConstructor —— 生成全参构造 (方便测试)
 *   违纪处理 —— violationCount (违纪次数) + violationScore (违纪扣分), 通过数据库原子操作更新
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Integer id;         // ID, 主键 (新增时 null 表示由数据库自增)
    private String name;        // 姓名
    private String no;          // 学号 (唯一约束, CHAR(10) 定长)
    private Integer gender;     // 性别, 1: 男, 2: 女
    private String phone;       // 手机号 (唯一约束)
    private String idCard;      // 身份证号 (唯一约束, CHAR(18))
    private Integer isCollege;  // 是否来自于院校, 1:是, 0:否
    private String address;     // 联系地址
    private Integer degree;     // 最高学历, 1:初中, 2:高中, 3:大专, 4:本科, 5:硕士, 6:博士
    private LocalDate graduationDate;  // 毕业时间
    private Integer clazzId;    // 班级ID, 关联班级表 (外键)
    private Integer violationCount;  // 违纪次数 (默认0)
    private Integer violationScore;  // 违纪扣分 (默认0)
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 修改时间

    // 以下字段仅用于数据展示, 不对应数据库列
    private String clazzName; // 班级名称 (联表查询 clazz 表获得)
}
