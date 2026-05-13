package com.itheima.service.impl;

import com.itheima.mapper.EmpMapper;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.ClazzCountOption;
import com.itheima.pojo.JobOption;
import com.itheima.service.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 数据统计 Service 实现
 *
 * 知识点:
 *   @Service —— Spring IOC 管理
 *   Stream API —— Java 8 流式操作, map 提取字段, toList 收集结果
 *   聚合查询 —— 使用 SQL 的 COUNT / GROUP BY / CASE WHEN 进行数据统计
 */
@Service
public class ReportService implements ReportServiceImpl {

    @Autowired
    public EmpMapper empMapper;         // 员工数据访问

    @Autowired
    public StudentMapper studentMapper; // 学员数据访问

    /**
     * 统计各个职位的员工人数
     * 知识点: Stream.map —— 从 Map 中提取特定字段, 转换为两个平行 List
     *   jobList: 职位名称列表 (X 轴)
     *   dataList: 对应人数列表 (Y 轴)
     */
    @Override
    public JobOption getEmpJobData() {
        // 从数据库查询各职位人数 (SQL 中 CASE WHEN 转换编码为文字)
        List<Map<String, Object>> list = empMapper.countEmpJobData();
        // 提取职位名称列表 (pos 列)
        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        // 提取对应人数列表 (total 列)
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("total")).toList();
        return new JobOption(jobList, dataList);
    }

    /**
     * 统计员工性别分布
     * 知识点: 直接返回 List<Map>, 每个 Map 含 name(性别) 和 value(人数)
     *   直接用于前端 ECharts 饼图渲染
     */
    @Override
    public List<Map> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }

    /**
     * 统计学员学历分布
     * 知识点: 直接返回 List<Map>, key 为 name(学历名) 和 value(人数)
     *   由 Spring MVC 自动序列化为 JSON
     */
    @Override
    public List<Map> getStudentDegreeData() {
        return studentMapper.countStudentDegreeData();
    }

    /**
     * 统计每个班级的学员人数
     * 知识点: 从查询结果 Map 中提取班级名称列表和人数列表
     *   前端 ECharts 需要两个平行数组: clazzList(班级名) + dataList(人数)
     */
    @Override
    public ClazzCountOption getStudentCountData() {
        // 查询各班级学员人数 (LEFT JOIN 关联, 包含学员数为 0 的班级)
        List<Map> list = studentMapper.countStudentCountData();
        // 提取班级名称列表 (name 列)
        List<Object> clazzList = list.stream().map(dataMap -> dataMap.get("name")).toList();
        // 提取对应人数列表 (value 列)
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("value")).toList();
        return new ClazzCountOption(clazzList, dataList);
    }
}
