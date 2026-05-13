package com.itheima.service;

import com.itheima.pojo.JobOption;

import java.util.List;
import java.util.Map;

/**
 * @Description ReportServiceImpl
 * @Author Mikey
 * @Date 2026-05-12  18:14
 */
public interface ReportServiceImpl {
    JobOption getEmpJobData();

    List<Map> getEmpGenderData();
}
