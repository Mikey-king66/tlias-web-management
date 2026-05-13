package com.itheima.service;

import com.itheima.pojo.ClazzCountOption;
import com.itheima.pojo.JobOption;

import java.util.List;
import java.util.Map;

/**
 * 数据统计 Service 接口
 *
 * 知识点:
 *   面向接口编程 —— Controller 依赖接口, 解耦具体实现
 *   List<Map> 通用数据结构 —— 灵活的数据格式, 直接对接前端 ECharts 图表
 *   聚合查询 —— 使用 SQL 的 COUNT / GROUP BY / CASE WHEN 进行数据统计
 */
public interface ReportServiceImpl {

    /**
     * 统计各个职位的员工人数
     * 知识点: JobOption 封装两个平行 List (职位列表 + 人数列表), 对应 ECharts 柱状图 X/Y 轴
     */
    JobOption getEmpJobData();

    /**
     * 统计员工性别分布
     * 知识点: List<Map> 格式, 每条 Map 含 name(性别) 和 value(人数), 对应 ECharts 饼图
     */
    List<Map> getEmpGenderData();

    /**
     * 统计学员学历分布
     * 知识点: List<Map> —— 通用数据结构, 每条 Map 包含 name(学历) 和 value(人数)
     */
    List<Map> getStudentDegreeData();

    /**
     * 统计每个班级的学员人数
     * 知识点: ClazzCountOption 封装两个 List (班级名称列表 + 各班级人数), 匹配前端 ECharts 柱状图
     */
    ClazzCountOption getStudentCountData();
}
