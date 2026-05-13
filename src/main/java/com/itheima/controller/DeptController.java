package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 部门管理Controller
 */
@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 查询部门
     */
    @GetMapping //限定当前请求的请求方式为GET
    public Result list() throws Exception {
        List<Dept> deptList = deptService.list();
        return Result.success(deptList);
    }

    /**
     * 根据ID删除部门 - 接收请求参数 - 方式一: 原始 HttpServletRequest 对象获取 (少用)
     */
    /*@DeleteMapping("/depts")
    public Result delete(HttpServletRequest request){
        String id = request.getParameter("id");
        int _id = Integer.parseInt(id);
        System.out.println("根据ID删除部门 : " + _id);

        return Result.success();
    }*/

    /**
     * 根据ID删除部门 - 接收请求参数 - 方式二: @RequestParam注解绑定请求参数到方法形参
     *
     * 场景: 前端传递参数名与controller方法形参不一致
     */
    /*@DeleteMapping("/depts")
    public Result delete2(@RequestParam(name = "id", required = false) Integer _id){
        System.out.println("@RequestParam 根据ID删除部门 : " + _id);
        return Result.success();
    }*/


    /**
     * 根据ID删除部门 - 接收请求参数 - 方式三: 前端传递参数名与controller方法形参名一致, 省略 @RequestParam (推荐的方式)
     * 场景: 前端传递参数名与controller方法形参一致
     */
    @DeleteMapping
    public Result delete(Integer id){
        log.info(" 根据ID删除部门 : " + id);
        deptService.delete(id);
        return Result.success();
    }


    /**
     * 新增部门
     *  {"name": "...."}  ---> @RequestBody 对象
     */
    @PostMapping
    public Result add(@RequestBody Dept dept){
        log.info(" 新增部门 : " + dept);
        deptService.add(dept);
        return Result.success();
    }


    /**
     * 根据ID查询部门 -- /depts/10
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info(" 根据ID查询部门 : " + id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }


    /**
     * 修改部门
     */
    @PutMapping
    public Result update(@RequestBody Dept dept){
        log.info(" 修改部门 : " + dept);
        deptService.update(dept);
        return Result.success();
    }


}
