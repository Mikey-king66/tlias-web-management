package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 部门实体类
 *
 * 知识点:
 *   Lombok —— @Data + @NoArgsConstructor + @AllArgsConstructor
 *   Java 8 LocalDateTime —— 线程安全、不可变的时间类, 替代传统的 java.util.Date
 *   包装类型 Integer —— 推荐用包装类型, 避免基本类型默认值 0 误插入数据库
 *   MyBatis 下划线转驼峰自动映射 —— 数据库字段 create_time 自动映射到 createTime
 *     (通过 mybatis.configuration.map-underscore-to-camel-case=true 配置)
 */
@Data
@NoArgsConstructor // 无参构造
@AllArgsConstructor // 全参构造
public class Dept {
    private Integer id; // 主键 (推荐使用包装类型, 基本类型有默认值 0 会产生歧义)
    private String name; // 部门名称
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 修改时间
}
