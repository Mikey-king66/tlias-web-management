package com.itheima.mapper;

import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 部门管理 Mapper 接口
 *
 * 知识点:
 *   @Mapper —— MyBatis 映射器注解, 标识该接口由 MyBatis 动态代理实现并注入 Spring 容器
 *   @Select / @Insert / @Delete / @Update —— MyBatis 注解式 SQL, 适用于简单 CRUD 操作
 *   XML 映射 —— 复杂 SQL (动态更新) 使用 XML 配置, 与注解混合使用
 *   下划线转驼峰 —— 通过 mybatis 配置 map-underscore-to-camel-case=true 自动映射
 *     数据库字段 create_time 自动映射到 Java 属性 createTime
 */
@Mapper
public interface DeptMapper {

    /**
     * 查询所有部门
     * 知识点: @Select 注解式查询 SQL
     *   下划线转驼峰方案对比:
     *     方式一: @Results 手动映射 (代码繁琐)
     *     方式二: SQL 起别名 create_time as createTime (SQL 冗余)
     *     方式三 (推荐): map-underscore-to-camel-case=true 全局配置, 代码最简洁
     */
    //方式一 : @Results手动映射
    //@Results({
    //        @Result(column = "create_time", property = "createTime"),
    //        @Result(column = "update_time", property = "updateTime")
    //})

    //方式二 : 起别名
    //@Select("select id, name, create_time createTime, update_time updateTime from dept")

    @Select("select id, name, create_time, update_time from dept")
    public List<Dept> findAll();

    /**
     * 根据ID删除部门
     * 知识点: @Delete —— 注解式删除, #{id} 占位符接收方法参数
     *   注意事项: 删除前需校验该部门下是否有员工, 否则可能违反外键约束
     */
    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);

    /**
     * 新增部门
     * 知识点: @Insert —— 注解式插入, #{属性名} 从 Dept 对象取值 (底层 PreparedStatement 防 SQL 注入)
     *   create_time/update_time 需在 Service 层手动设置为当前时间
     */
    @Insert("insert into dept(name, create_time, update_time) values (#{name},#{createTime},#{updateTime})")
    void insert(Dept dept);

    /**
     * 根据ID查询部门详情
     * 知识点: @Select —— 注解式查询, where 条件匹配单条记录, MyBatis 自动封装结果集
     */
    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getById(Integer id);

    /**
     * 根据ID动态更新部门数据 (XML 实现)
     * 知识点: XML 中使用 set + if 标签实现动态更新
     *   @Update(全部字段更新) vs XML 动态更新(只更新非空字段)
     *   动态更新优势: 避免前端未传的字段被 null 覆盖数据库原有数据
     */
    //@Update("update dept set name = #{name} , update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);
}
