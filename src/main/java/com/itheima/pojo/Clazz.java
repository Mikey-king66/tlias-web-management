package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 班级实体类
 *
 * 知识点:
 *   Lombok 三件套 —— @Data 生成 getter/setter/toString/equals/hashCode,
 *     @NoArgsConstructor 生成无参构造 (反序列化需要), @AllArgsConstructor 生成全参构造 (方便测试)
 *   Java 8 时间 API —— LocalDate (仅日期, 无时分秒), LocalDateTime (日期+时间), 线程安全不可变
 *   包装类型 Integer vs 基本类型 int —— 推荐用包装类型, null 表示"未设置", 避免默认值0的歧义
 *   实体字段分两类:
 *     - 数据库字段: id/name/room/beginDate 等, MyBatis 自动映射
 *     - 展示字段: masterName (联表查询获得), status (业务层计算, 不存数据库)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clazz {
    private Integer id; // ID, 主键 (包装类型, 新增时 null 表示由数据库自增)
    private String name; // 班级名称
    private String room; // 班级教室
    private LocalDate beginDate; // 开课时间 (LocalDate: 仅日期, 无时分秒)
    private LocalDate endDate;   // 结课时间
    private Integer masterId;    // 班主任ID, 关联员工表 (外键)
    private Integer subject;     // 学科, 1:Java, 2:前端, 3:大数据, 4:Python, 5:Go, 6:嵌入式
    private LocalDateTime createTime; // 创建时间 (LocalDateTime: 日期+时间)
    private LocalDateTime updateTime; // 修改时间

    // 以下字段仅用于数据展示, 不对应数据库列
    private String masterName; // 班主任姓名 (联表查询 emp 表获得, MyBatis 自动映射)
    private String status;     // 班级状态 (Java 业务层计算: 未开班 / 已开班 / 已结课)
}
