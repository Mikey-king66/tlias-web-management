package com.itheima.mapper;

import com.itheima.pojo.EmpLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper 接口 (记录员工操作变更)
 *
 * 知识点:
 *   @Mapper —— MyBatis 映射器注解, 由 MyBatis 动态代理实现并注入 Spring 容器
 *   注解式 SQL —— 使用 @Insert 注解声明插入语句, 适用于简单 SQL
 *   事务传播行为 —— Propagation.REQUIRES_NEW 在 Service 层配合使用, 确保日志独立提交
 */
@Mapper
public interface EmpLogMapper {

    /**
     * 插入操作日志记录
     * 知识点:
     *   @Insert —— MyBatis 注解式插入, #{属性名} 从实体对象取值 (PreparedStatement 防 SQL 注入)
     *   独立事务 —— Service 层配合 @Transactional(propagation = Propagation.REQUIRES_NEW)
     *     确保日志操作独立提交, 不受主事务回滚影响
     */
    @Insert("insert into emp_log (operate_time, info) values (#{operateTime}, #{info})")
    public void insert(EmpLog empLog);

}
