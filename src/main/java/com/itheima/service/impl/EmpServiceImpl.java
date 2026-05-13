package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpLogMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.*;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;

    /**
     * 原始方式
     */
    /*@Override
    public PageBean page(Integer page, Integer pageSize) {
        //1. 调用mapper接口获取总记录数 total
        Long total = empMapper.count();

        //2. 调用mapper接口获取结果列表 rows
        //计算起始索引
        Integer start = (page - 1) * pageSize;
        List<Emp> rows = empMapper.list(start, pageSize);

        //3. 封装结果
        return new PageBean(total, rows);
    }*/


    @Override
    public PageBean page(EmpQueryParam queryParam) {
        //1. 设置分页参数 - PageHelper
        PageHelper.startPage(queryParam.getPage(), queryParam.getPageSize());

        //2. 执行查询
        List<Emp> empList = empMapper.list(queryParam);

        //3. 解析结果
        Page<Emp> p = (Page<Emp>) empList;
        return new PageBean(p.getTotal(), p.getResult());
    }

    @Autowired
    private EmpLogService empLogService;

    @Transactional(rollbackFor = {Exception.class}) //事务管理的注解 - 所有异常都会回滚
    @Override
    public void add(Emp emp) throws Exception {
        try {
            //1. 调用empMapper保存员工的基本信息
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);
            //int i = 1/0;
            //2. 调用empExprMapper保存员工的工作经历信息
            List<EmpExpr> exprList = emp.getExprList();
            if(!CollectionUtils.isEmpty(exprList)){
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
            }
            empExprMapper.insertBatch(exprList); //批量保存工作经历信息
        } finally {
            //记录新增员工的操作日志
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), emp.toString());
            empLogService.insertLog(empLog);
        }
    }

    @Override
    public void deleteById(List<Integer> ids) {
        //批量删除员工
        empMapper.deleteByIds(ids);

        //批量删除工作经历
        empExprMapper.deleteByEmpIds(ids);
    }

    @Override
    public Emp getInfo(Integer id) {
        //返回查询 封装好的数据
        return empMapper.getById(id);
    }

    @Override
    public void update(Emp emp) {
        //根据id更新员工基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        //根据员工ID删除员工的工作经历信息 【删除老的】
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));

        // 新增员工的工作经历数据 【新增新的】
        //获取工作经历集合
        Integer empId = emp.getId();
        List<EmpExpr> exprList = emp.getExprList();
        // 循环设置id
        if (CollectionUtils.isEmpty(exprList)){
            exprList.forEach(empExpr -> {
                empExpr.setEmpId(empId);
            });

            //添加新的工作经历
            empExprMapper.insertBatch(exprList);
        }

    }

}
