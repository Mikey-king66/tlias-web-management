package com.itheima.mapper;

import com.itheima.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 员工工作经历 Mapper 接口
 *
 * 知识点:
 *   @Mapper —— MyBatis 映射器注解, 由 MyBatis 动态代理实现并注入 Spring 容器
 *   批量操作 —— 使用 XML 中的 foreach 标签实现批量插入和批量删除
 *   参数绑定 —— 集合类型参数传入 XML, 由 MyBatis 自动封装为可遍历对象
 */
@Mapper
public interface EmpExprMapper {

    /**
     * 批量保存员工工作经历 (XML 实现, foreach 标签)
     * 知识点:
     *   foreach 标签遍历 exprList 集合实现批量 INSERT
     *   collection —— 遍历的集合参数名
     *   item —— 每次遍历的元素别名, 通过 #{expr.属性名} 取值
     *   separator —— 每个值组之间用逗号分隔, 拼接成单条批量 INSERT 语句
     */
    void insertBatch(List<EmpExpr> exprList);

    /**
     * 根据员工ID集合批量删除工作经历 (XML 实现, foreach 标签)
     * 知识点:
     *   foreach 遍历ID集合生成 in (...) 子句
     *   open="(" / close=")" —— 在循环前后拼接括号
     *   separator="," —— 多个ID之间用逗号分隔
     */
    void deleteByEmpIds(List<Integer> empIds);
}
