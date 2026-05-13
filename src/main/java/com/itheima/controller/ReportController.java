package com.itheima.controller;

import com.itheima.pojo.JobOption;
import com.itheima.pojo.Result;
import com.itheima.service.ReportServiceImpl;
import com.itheima.service.impl.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * @Description ReportController
 * @Author Mikey
 * @Date 2026-05-12  18:12
 */
@RequestMapping("/report")
@RestController
@Transactional
public class ReportController {

    @Autowired
    public ReportService reportService;

    /**
     * 部门人数统计图
     * @return
     */
    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        JobOption jobOption  = reportService.getEmpJobData();
        return Result.success(jobOption);
    }

    // 部门性别统计图
    @GetMapping("/empGenderData")
    public Result getEmpGenderData(){
        List<Map> genderList = reportService.getEmpGenderData();
        return Result.success(genderList);
    }
}
