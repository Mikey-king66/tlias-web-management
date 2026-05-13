package com.itheima.controller;

import com.itheima.pojo.ClazzCountOption;
import com.itheima.pojo.JobOption;
import com.itheima.pojo.Result;
import com.itheima.service.ReportServiceImpl;
import com.itheima.service.impl.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 数据统计 Controller
 * 接口前缀: /report
 *
 * 知识点:
 *   @RestController —— 返回 JSON 格式数据
 *   @RequestMapping("/report") —— 类级别请求路径映射
 *   GET 请求: 统计类接口通常使用 GET 请求, 无需复杂参数
 *   ECharts 数据格式: 返回特定格式的数据供前端图表库 (ECharts) 渲染
 *   聚合查询: Service 层使用 SQL 聚合函数 (COUNT / GROUP BY) 进行数据统计
 *   DI 依赖注入 —— @Autowired 自动注入 ReportService
 */
@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    public ReportService reportService;

    /**
     * 员工职位人数统计图 (对应 ECharts 柱状图)
     * GET /report/empJobData
     *
     * 知识点: 返回两个平行数组 (职位列表 + 人数列表), 对应 ECharts 柱状图 X 轴和 Y 轴数据
     *   数据库使用 SELECT job, COUNT(*) GROUP BY job 聚合查询
     */
    @GetMapping("/empJobData")
    public Result getEmpJobData() {
        JobOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
    }

    /**
     * 员工性别统计图 (对应 ECharts 饼图)
     * GET /report/empGenderData
     *
     * 知识点: 返回 List<Map> 格式, 对应 ECharts 饼图数据格式
     *   每个数据项: {name: "男", value: 12}, {name: "女", value: 8}
     *   数据库使用 SELECT gender, COUNT(*) GROUP BY gender 聚合查询
     */
    @GetMapping("/empGenderData")
    public Result getEmpGenderData() {
        List<Map> genderList = reportService.getEmpGenderData();
        return Result.success(genderList);
    }

    /**
     * 学员学历统计图 (对应 ECharts 饼图)
     * GET /report/studentDegreeData
     *
     * 知识点: 与性别统计返回格式相同, 使用 List<Map> 通用结构
     *   数据项示例: {name: "本科", value: 30}, {name: "硕士", value: 15}
     *   数据库使用 SELECT degree, COUNT(*) GROUP BY degree 聚合查询
     */
    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData() {
        List<Map> degreeList = reportService.getStudentDegreeData();
        return Result.success(degreeList);
    }

    /**
     * 班级人数统计图 (对应 ECharts 柱状图)
     * GET /report/studentCountData
     *
     * 知识点: 返回班级名称列表和人数列表两个平行数组, 对应 ECharts 柱状图 X/Y 轴
     *   多表关联查询: 使用 LEFT JOIN 关联 clazz 表和 student 表, GROUP BY clazz.id 统计
     *   用于展示每个班级的学员数量分布
     */
    @GetMapping("/studentCountData")
    public Result getStudentCountData() {
        ClazzCountOption countData = reportService.getStudentCountData();
        return Result.success(countData);
    }
}
