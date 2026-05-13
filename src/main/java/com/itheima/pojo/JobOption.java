package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 员工职位人数统计 (ECharts 图表数据格式)
 *
 * 知识点:
 *   前端 ECharts 图表标准数据格式 —— jobList 为 X 轴标签数组, dataList 为 Y 轴数值数组
 *   两个 List 通过索引一一对应 (如 jobList[0] 对应 dataList[0])
 *   后端通过 SQL 的 CASE WHEN + GROUP BY 聚合查询返回此类数据
 *   类似模式广泛应用于各类统计报表接口 (性别统计、学历统计等)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOption {
    private List jobList;  // 职位列表 (X轴: 班主任、讲师、学工主管...)
    private List dataList; // 人数列表 (Y轴: 各职位对应人数)
}
