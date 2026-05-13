package com.itheima.mapper;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 班级管理 Mapper 接口
 *
 * 知识点:
 *   @Mapper —— MyBatis 映射器注解, 将该接口标识为 Mapper, 由 Spring 管理并创建代理对象
 *   注解方式与 XML 方式混用 —— 简单 SQL 使用注解 (@Insert / @Select), 复杂 SQL / 动态 SQL 使用 XML 配置
 *   @Options(useGeneratedKeys=true) —— 主键回填, 插入后自动获取自增 ID
 *   LEFT JOIN 联表查询 —— 班级表 LEFT JOIN 员工表获取班主任姓名
 */
@Mapper
public interface ClazzMapper {

    /**
     * 条件分页查询班级列表 (XML 实现, 支持动态条件 + LEFT JOIN 联表查询班主任)
     * 知识点: PageHelper 分页插件自动拦截下一条 SQL, 拼接 limit 分页子句
     *   动态 SQL —— where 标签自动生成 where 关键字, if 标签按条件拼接
     *   模糊查询 —— concat('%', #{name}, '%') 实现名字模糊匹配
     */
    List<Clazz> list(ClazzQueryParam queryParam);

    /**
     * 根据ID查询班级详情 (XML 实现, LEFT JOIN 联表查询班主任姓名)
     * 知识点: 左外连接 LEFT JOIN —— 即使班主任ID为空也能查出班级基本信息
     */
    Clazz getById(Integer id);

    /**
     * 新增班级
     * 知识点:
     *   @Options(useGeneratedKeys=true, keyProperty="id") —— 自动获取数据库自增主键并回填到实体对象
     *   @Insert —— 声明式插入 SQL, #{属性名} 从实体取值 (底层 PreparedStatement 防 SQL 注入)
     *   keyProperty 指定主键回填的目标属性名
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into clazz (name, room, begin_date, end_date, master_id, subject, create_time, update_time) " +
            "values (#{name}, #{room}, #{beginDate}, #{endDate}, #{masterId}, #{subject}, #{createTime}, #{updateTime})")
    void insert(Clazz clazz);

    /**
     * 动态更新班级信息 (XML 实现, set + if 标签按需更新)
     * 知识点: set 标签生成 set 关键字, 自动去除更新字段之后多余的逗号
     *   if 标签按条件判断是否更新某字段, 只更新非空字段
     */
    void update(Clazz clazz);

    /**
     * 根据ID删除班级 (XML 实现)
     * 知识点: 按主键删除单条记录
     *   注意: 删除前需校验该班级下是否有学员 (通过 countStudentsByClazzId)
     */
    void deleteById(Integer id);

    /**
     * 查询全部班级列表 (注解方式)
     * 知识点: @Select 注解 —— 声明式查询 SQL, 适用于固定查询
     *   LEFT JOIN + 别名 —— e.name as masterName 联表查询班主任姓名
     *   排序 —— order by update_time desc 按更新时间降序排列
     */
    @Select("select c.id, c.name, c.room, c.begin_date, c.end_date, c.master_id, c.subject, c.create_time, c.update_time, e.name as masterName " +
            "from clazz c left join emp e on c.master_id = e.id order by c.update_time desc")
    List<Clazz> findAll();

    /**
     * 统计某班级下的学员人数 (用于删除前校验)
     * 知识点: COUNT(*) 聚合函数统计学员数量
     *   返回值 int 可直接用于判断: 若大于 0 则不允许删除
     */
    int countStudentsByClazzId(Integer clazzId);
}
