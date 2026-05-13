package com.itheima.service;

import com.itheima.pojo.PageBean;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;

import java.util.List;

/**
 * 学员管理 Service 接口
 *
 * 知识点:
 *   面向接口编程 —— Controller 只依赖接口, 降低耦合
 *   接口定义业务规范, 便于后续扩展 (如增加缓存、切换 ORM 框架)
 */
public interface StudentService {

    /**
     * 条件分页查询学员 (支持按姓名模糊查 + 学历/班级精确匹配)
     */
    PageBean page(StudentQueryParam queryParam);

    /**
     * 新增学员 (自动补全时间戳和违纪默认值)
     */
    void save(Student student);

    /**
     * 根据ID查询学员 (含班级名称联表展示)
     */
    Student getById(Integer id);

    /**
     * 修改学员信息 (动态更新, 仅更新非空字段)
     */
    void update(Student student);

    /**
     * 批量删除学员 (支持一次删除多个)
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 违纪处理: 扣分 + 增加违纪次数
     * 知识点: 数据库层面原子更新 —— 一条 SQL 完成 violation_count+1 和 violation_score 累加
     */
    void violation(Integer id, Integer score);
}
