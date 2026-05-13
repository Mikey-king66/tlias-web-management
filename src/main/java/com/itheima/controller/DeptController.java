package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理 Controller
 * 接口前缀: /depts
 *
 * 知识点:
 *   @Slf4j —— Lombok 日志框架, 编译时生成 log 对象, 用于记录操作日志
 *   @RestController = @Controller + @ResponseBody, 所有方法返回值自动序列化为 JSON
 *   @RequestMapping("/depts") —— 类级别请求路径映射, 统一接口前缀
 *   RESTful API 设计: GET 查询 / POST 新增 / PUT 修改 / DELETE 删除
 *   @RequestBody —— JSON 请求体反序列化为 Java 对象 (依赖 Jackson)
 *   @PathVariable —— URL 路径变量绑定到方法参数
 *   DI 依赖注入 —— @Autowired 自动注入 DeptService
 *   全局统一响应 —— 所有接口返回 Result 包装类
 */
@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 查询所有部门列表
     * GET /depts
     *
     * 知识点: @GetMapping —— 限定 HTTP GET 请求, 无参数查询全量数据
     *   所有接口统一返回 Result 包装类, 前端根据 code 字段判断请求是否成功
     */
    @GetMapping // 限定当前请求的请求方式为 GET
    public Result list() throws Exception {
        List<Dept> deptList = deptService.list();
        return Result.success(deptList);
    }

    /**
     * 根据ID删除部门
     * DELETE /depts?id=xxx
     *
     * 知识点: Spring MVC 请求参数绑定 —— 参数名与方法形参名一致时自动绑定, 无需额外注解
     *   推荐方式: 代码简洁, 适用于简单类型参数接收
     *   删除前会校验部门下是否有员工, 有则抛异常阻止删除
     */
    /* 方式一: 原始 HttpServletRequest 对象获取 (少用)
    @DeleteMapping("/depts")
    public Result delete(HttpServletRequest request){
        String id = request.getParameter("id");
        int _id = Integer.parseInt(id);
        System.out.println("根据ID删除部门 : " + _id);
        return Result.success();
    }*/

    /* 方式二: @RequestParam 注解绑定请求参数到方法形参
       场景: 前端传递参数名与 controller 方法形参不一致时使用
    @DeleteMapping("/depts")
    public Result delete2(@RequestParam(name = "id", required = false) Integer _id){
        System.out.println("@RequestParam 根据ID删除部门 : " + _id);
        return Result.success();
    }*/

    @DeleteMapping
    public Result delete(Integer id) {
        log.info(" 根据ID删除部门 : " + id);
        deptService.delete(id);
        return Result.success();
    }

    /**
     * 新增部门
     * POST /depts
     * 请求体JSON: {"name": "部门名称"}
     *
     * 知识点: @RequestBody —— 将 HTTP 请求体中的 JSON 字符串反序列化为 Dept Java 对象
     *   前端必须设置 Content-Type: application/json
     *   Jackson 库自动处理 JSON 与 Java 对象之间的相互转换
     */
    @PostMapping
    public Result add(@RequestBody Dept dept) {
        log.info(" 新增部门 : " + dept);
        deptService.add(dept);
        return Result.success();
    }

    /**
     * 根据ID查询部门
     * GET /depts/{id}
     *
     * 知识点: @PathVariable —— 将 URL 路径中的 {id} 占位符绑定到方法参数
     *   RESTful 风格 API: 资源 ID 放在 URL 路径中而非 queryString 中
     *   路径参数通过大括号 {} 占位, Spring MVC 自动解析
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info(" 根据ID查询部门 : " + id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    /**
     * 修改部门
     * PUT /depts
     * 请求体JSON: {"id": 1, "name": "更新后的部门名称"}
     *
     * 知识点: @PutMapping —— RESTful 规范中 PUT 表示更新资源, 幂等操作 (多次执行结果一致)
     *   完整替换: PUT 通常要求传递完整资源对象
     *   @RequestBody —— 请求体 JSON 反序列化为 Java 对象
     */
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        log.info(" 修改部门 : " + dept);
        deptService.update(dept);
        return Result.success();
    }

}
