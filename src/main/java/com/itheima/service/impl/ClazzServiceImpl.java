package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.ClazzMapper;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.PageBean;
import com.itheima.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 班级管理 Service 实现
 *
 * 知识点:
 *   @Service —— Spring 业务层组件, 由 IOC 容器管理
 *   @Autowired —— 依赖注入, 自动装配 Mapper
 *   PageHelper —— 分页插件, startPage 设置分页参数, 自动拦截下一条 SQL
 *   业务逻辑计算 —— 班级状态不在数据库中存储, 由开课/结课时间与当前日期比较动态计算
 */
@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    /**
     * 条件分页查询班级
     * 知识点:
     *   PageHelper.startPage() —— 设置分页参数, 底层通过 ThreadLocal 传递, 线程安全
     *   查询结果强转为 Page 类型 —— Page 是 PageHelper 提供的 List 子类
     *   状态计算 —— 遍历结果集, 根据当前日期与开课/结课时间计算每个班级的状态
     */
    @Override
    public PageBean page(ClazzQueryParam queryParam) {
        //1. 设置分页参数
        PageHelper.startPage(queryParam.getPage(), queryParam.getPageSize());

        //2. 执行查询 (PageHelper 自动拦截并追加 limit)
        List<Clazz> clazzList = clazzMapper.list(queryParam);

        //3. 计算每个班级的状态并设置
        // 知识点: 业务逻辑计算 - 状态字段不在数据库中存储, 由开课/结课时间动态计算
        LocalDate today = LocalDate.now();
        clazzList.forEach(clazz -> clazz.setStatus(calcStatus(clazz, today)));

        //4. 解析分页结果
        Page<Clazz> p = (Page<Clazz>) clazzList;
        return new PageBean(p.getTotal(), p.getResult());
    }

    /**
     * 计算班级状态
     * 知识点: LocalDate.isBefore / isAfter —— Java 8 日期 API 比较, 比 Date 更直观安全
     *   未开班: 当前日期 < 开课日期
     *   已开班: 开课日期 <= 当前日期 <= 结课日期
     *   已结课: 当前日期 > 结课日期
     */
    private String calcStatus(Clazz clazz, LocalDate today) {
        if (today.isBefore(clazz.getBeginDate())) {
            return "未开班";
        } else if (today.isAfter(clazz.getEndDate())) {
            return "已结课";
        } else {
            return "已开班";
        }
    }

    /**
     * 新增班级
     * 知识点: 补全 createTime 和 updateTime 基础属性后调用 Mapper
     */
    @Override
    public void save(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    /**
     * 根据ID查询班级 (含状态计算)
     * 知识点: 查询后动态计算状态, 保证状态始终与实际日期保持一致
     */
    @Override
    public Clazz getById(Integer id) {
        Clazz clazz = clazzMapper.getById(id);
        if (clazz != null) {
            clazz.setStatus(calcStatus(clazz, LocalDate.now()));
        }
        return clazz;
    }

    /**
     * 修改班级信息
     * 知识点: 补全更新时间, 动态 SQL 只更新非空字段
     */
    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    /**
     * 根据ID删除班级
     * 知识点: 业务校验 —— 删除前检查关联数据, 有学员则抛异常阻止删除
     *   异常由 GlobalExceptionHandler 统一捕获, 返回 Result.error(msg) 给前端
     */
    @Override
    public void delete(Integer id) {
        // 校验该班级下是否有学员
        int count = clazzMapper.countStudentsByClazzId(id);
        if (count > 0) {
            throw new RuntimeException("对不起, 该班级下有学生, 不能直接删除");
        }
        clazzMapper.deleteById(id);
    }

    /**
     * 查询全部班级 (含状态计算)
     * 知识点: 遍历计算每个班级的状态, 适用于下拉列表展示
     */
    @Override
    public List<Clazz> findAll() {
        List<Clazz> clazzList = clazzMapper.findAll();
        LocalDate today = LocalDate.now();
        clazzList.forEach(clazz -> clazz.setStatus(calcStatus(clazz, today)));
        return clazzList;
    }
}
