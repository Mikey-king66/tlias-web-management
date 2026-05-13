package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 员工管理 Mapper 接口
 *
 * 知识点:
 *   @Mapper —— MyBatis 映射器注解, 由 MyBatis 动态代理实现并注入 Spring 容器
 *   注解 + XML 混用 —— 简单 SQL 使用 @Insert / @Select / @Update 注解, 复杂 SQL / 动态 SQL 在 XML 中定义
 *   @Options(useGeneratedKeys=true) —— 主键回填, 插入后获取自增 ID 赋值给实体对象
 *   ResultMap —— XML 中定义复杂结果映射, collection 处理一对多关联 (员工 → 工作经历列表)
 */
@Mapper
public interface EmpMapper {

    /**
     * 条件分页查询员工列表 (XML 实现, 支持动态条件)
     * 知识点: PageHelper 分页插件自动拦截 SQL 添加 limit 子句
     *   动态 SQL —— where 标签自动生成 where 关键字并去除多余 and/or
     */
    List<Emp> list(EmpQueryParam queryParam);

    /**
     * 新增员工
     * 知识点:
     *   @Options(useGeneratedKeys=true, keyProperty="id") —— 主键回填
     *     插入后自动获取数据库自增主键值, 赋值给 emp 对象的 id 属性
     *   @Insert —— 注解式插入, #{属性名} 占位符取值 (PreparedStatement 防 SQL 注入)
     *   注意: 插入前需在 Service 层设置 createTime 和 updateTime
     */
    @Options(useGeneratedKeys = true, keyProperty = "id") // 获取数据库自增主键, 赋值给 emp 对象的 id 属性
    @Insert("insert into emp (username, password, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) values " +
            "(#{username}, #{password}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);

    /**
     * 批量删除员工 (XML 实现, foreach 标签)
     * 知识点: foreach 遍历 ids 集合生成 in (?,?,?) 子句
     *   注意: 删除前需先删除关联表数据 (如工作经验), 否则违反外键约束
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 根据ID查询员工详情 (XML 实现, ResultMap + collection 一对多映射)
     * 知识点:
     *   ResultMap 自定义结果映射 —— 处理数据库字段与实体属性不一致
     *   collection 标签 —— 映射一对多关联, 如 员工 → 工作经历列表
     */
    Emp getById(Integer id);

    /**
     * 根据ID动态更新员工信息 (XML 实现, set + if 标签)
     * 知识点: set 标签 —— 生成 set 关键字并自动去除末尾多余的逗号
     *   if 标签 —— 按条件判断是否更新某字段, 实现部分更新
     */
    void updateById(Emp emp);

    /**
     * 统计各个职位的员工人数 (XML 实现)
     * 知识点: CASE WHEN —— SQL 条件表达式, 将数字编码转为可读文字
     *   GROUP BY —— 按职位分组, 配合 COUNT(*) 聚合统计
     */
    List<Map<String, Object>> countEmpJobData();

    /**
     * 统计员工性别分布 (XML 实现)
     * 知识点: if 函数 —— MySQL 内置条件函数, 根据 gender 值返回 '男' / '女'
     */
    List<Map> countEmpGenderData();

    /**
     * 查询所有员工 (注解方式)
     * 知识点: @Select —— 直接编写 SQL, LEFT JOIN dept 查询部门名称
     *   别名映射 —— dept.name as deptName 实现字段与实体属性映射
     */
    @Select("select emp.*, dept.name deptName from emp left join dept on emp.dept_id = dept.id order by emp.update_time desc")
    List<Emp> findAll();

    /**
     * 修改员工密码
     * 知识点: @Update —— 注解式更新 SQL
     *   now() 函数 —— MySQL 当前时间, 自动更新 update_time 字段
     */
    @Update("update emp set password = #{password}, update_time = now() where id = #{id}")
    void updatePassword(Emp emp);

    /**
     * 根据用户名查询员工 (用于登录验证)
     * 知识点: @Select —— 注解式查询, where 条件精确匹配用户名
     *   注意: 数据库 username 字段应有唯一约束, 否则登录逻辑可能出错
     */
    @Select("select * from emp where username = #{username}")
    Emp getByUsername(String username);
}
