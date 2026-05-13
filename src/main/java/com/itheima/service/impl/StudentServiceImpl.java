package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学员管理 Service 实现
 *
 * 知识点:
 *   @Service —— Spring IOC 容器管理, 默认单例
 *   PageHelper —— MyBatis 分页插件, startPage() 通过 ThreadLocal 传递分页参数
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 条件分页查询学员
     * 知识点:
     *   PageHelper.startPage() —— 设置页码和每页条数
     *   拦截下一条 SQL 自动追加 limit 分页子句
     *   Page 强转获取 total (总记录数) 和当前页数据
     */
    @Override
    public PageBean page(StudentQueryParam queryParam) {
        //1. 设置分页参数
        PageHelper.startPage(queryParam.getPage(), queryParam.getPageSize());

        //2. 执行查询 (PageHelper 自动拦截并追加 limit)
        List<Student> studentList = studentMapper.list(queryParam);

        //3. 解析分页结果
        Page<Student> p = (Page<Student>) studentList;
        return new PageBean(p.getTotal(), p.getResult());
    }

    /**
     * 新增学员
     * 知识点:
     *   补全 createTime / updateTime 以及默认值 (violationCount=0, violationScore=0)
     *   @Options(useGeneratedKeys=true) 配合主键回填, 插入后 student.getId() 获取自增主键
     */
    @Override
    public void save(Student student) {
        // 补全基础属性
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        // 设置违纪默认值 (新学员违纪次数和扣分为 0)
        if (student.getViolationCount() == null) {
            student.setViolationCount(0);
        }
        if (student.getViolationScore() == null) {
            student.setViolationScore(0);
        }
        studentMapper.insert(student);
    }

    /**
     * 根据ID查询学员 (含联表班级名称)
     * 知识点: 联表查询 —— LEFT JOIN clazz 获取班级名称, 用于编辑回显
     */
    @Override
    public Student getById(Integer id) {
        return studentMapper.getById(id);
    }

    /**
     * 修改学员信息
     * 知识点: 补全 updateTime 后调用动态更新
     *   XML 中 if 标签仅更新非空字段, 避免覆盖数据库原有数据
     */
    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    /**
     * 批量删除学员
     * 知识点: 接收 ID 集合, XML 中使用 foreach 拼接 IN (?,?,?) 实现批量操作
     */
    @Override
    public void deleteByIds(List<Integer> ids) {
        studentMapper.deleteByIds(ids);
    }

    /**
     * 违纪处理: 扣除分数并增加违纪次数
     * 知识点: 每次违纪 violation_count + 1, violation_score 累加扣分
     *   数据库层面原子更新 —— 一条 SQL 完成两个字段的更新, 避免并发问题
     */
    @Override
    public void violation(Integer id, Integer score) {
        studentMapper.updateViolation(id, score);
    }
}
