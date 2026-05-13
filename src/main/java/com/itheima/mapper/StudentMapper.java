package com.itheima.mapper;

import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 学员管理 Mapper 接口
 *
 * 知识点:
 *   @Mapper —— MyBatis 映射器注解, Spring 自动创建代理对象并注入 IOC 容器
 *   注解方式 (简单 CRUD) + XML 方式 (复杂查询) 混合使用
 *   @Param —— 多参数时必须使用此注解指定参数名, 否则 XML 中只能用 arg0/arg1 或 param1/param2 引用
 */
@Mapper
public interface StudentMapper {

    /**
     * 条件分页查询学员列表 (XML 实现, 动态条件 + 联表查询班级名称 + 按更新时间降序)
     */
    List<Student> list(StudentQueryParam queryParam);

    /**
     * 根据ID查询学员详情 (XML 实现, LEFT JOIN clazz 获取班级名称)
     */
    Student getById(Integer id);

    /**
     * 新增学员
     * 知识点:
     *   @Insert —— MyBatis 注解式 SQL, #{} 占位符从实体对象取值 (PreparedStatement 防 SQL 注入)
     *   @Options(useGeneratedKeys=true, keyProperty="id") —— 主键回填
     *     插入后自动获取数据库自增主键值, 赋值给 student 对象的 id 属性
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into student (name, no, gender, phone, id_card, is_college, address, degree, " +
            "graduation_date, clazz_id, violation_count, violation_score, create_time, update_time) " +
            "values (#{name}, #{no}, #{gender}, #{phone}, #{idCard}, #{isCollege}, #{address}, #{degree}, " +
            "#{graduationDate}, #{clazzId}, #{violationCount}, #{violationScore}, #{createTime}, #{updateTime})")
    void insert(Student student);

    /**
     * 动态更新学员信息 (XML 实现, set + if 标签按需更新)
     * 知识点: set 标签生成 set 关键字, 自动去除末尾多余逗号
     *   if 标签根据条件判断是否更新某字段
     */
    void update(Student student);

    /**
     * 批量删除学员 (XML 实现, foreach 遍历ID集合)
     * 知识点: foreach 标签遍历 ids 集合生成 in (...) 子句
     *   open/close 分别拼接左右括号, separator 指定分隔符
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 违纪处理: 扣分并增加违纪次数 (XML 实现)
     * 知识点:
     *   @Param —— 多参数时必须使用此注解指定参数名
   *     避免 XML 中通过 arg0/arg1 或 param1/param2 引用, 可读性差
     *   SQL 中直接做数值运算 —— violation_count + 1, violation_score + #{score}
     *     数据库层面原子更新, 避免并发问题
     */
    void updateViolation(@Param("id") Integer id, @Param("score") Integer score);

    /**
     * 统计学员学历分布 (XML 实现, 用于报表展示)
     * 知识点:
     *   CASE WHEN 表达式 —— 将数字学历编码转为可读文字 (1→初中, 2→高中...)
     *   GROUP BY + COUNT(*) —— 按学历分组统计各学历人数
     *   返回 List<Map>, 直接用于前端 ECharts 图表展示
     */
    List<Map> countStudentDegreeData();

    /**
     * 统计每个班级的学员人数 (XML 实现, 用于报表展示)
     * 知识点:
     *   LEFT JOIN —— 包含没有学员的班级 (学员人数为 0)
     *   COUNT(s.id) —— 统计学员数, 使用 s.id 可正确统计 NULL 情况
     *   GROUP BY + ORDER BY —— 按班级分组统计后按人数降序排列
     */
    List<Map> countStudentCountData();
}
