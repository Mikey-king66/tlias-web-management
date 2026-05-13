package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 班级人数统计 数据封装 (ECharts 柱状图)
 *
 * 知识点:
 *   前端 ECharts 需要两个平行数组格式的数据
 *     - clazzList: X 轴数据 (班级名称列表)
 *     - dataList:  Y 轴数据 (各班级人数)
 *   多表关联查询 —— 使用 LEFT JOIN 关联 clazz 表和 student 表, GROUP BY 统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClazzCountOption {
    private List clazzList; // 班级名称列表 (X轴)
    private List dataList;  // 各班级人数列表 (Y轴)
}
