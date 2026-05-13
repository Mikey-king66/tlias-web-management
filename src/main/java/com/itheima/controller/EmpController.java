package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequestMapping("/emps")
@RestController
@Transactional
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 分页查询
     */
   /* @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page ,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name , Integer gender,
                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin ,
                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("分页查询: {}, {}, {}, {}, {}, {}", page, pageSize, name, gender, begin, end);
        PageBean pageBean = empService.page(page, pageSize);
        return Result.success(pageBean);
    }*/


    /**
     * 分页查询
     */
    @GetMapping
    public Result page(EmpQueryParam queryParam){
        log.info("分页查询: {}", queryParam);
        PageBean pageBean = empService.page(queryParam);
        return Result.success(pageBean);
    }

    /**
     * 新增员工
     */
    @PostMapping
    public Result add(@RequestBody Emp emp) throws Exception {
        log.info("新增员工: {}" , emp);
        empService.add(emp);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) throws Exception {
        empService.deleteById(ids);
        return Result.success();
    }

    /**
     * 查询回显
     */
    @GetMapping ("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    /**
     * 更新员工信息
     */
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        empService.update(emp);
        return Result.success();
    }
}
