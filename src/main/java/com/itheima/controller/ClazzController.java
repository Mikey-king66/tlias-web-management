package com.itheima.controller;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 班级管理 Controller
 * 接口前缀: /clazzs
 *
 * 知识点:
 *   @Slf4j —— Lombok 日志注解, 编译时生成 log 对象, 使用 log.info/error 记录日志
 *   @RestController = @Controller + @ResponseBody, 所有方法返回值自动转为 JSON
 *   @RequestMapping —— 类级别的请求路径映射, 统一设置接口前缀
 *   RESTful 风格 API 设计: GET 查询 / POST 新增 / PUT 修改 / DELETE 删除
 *   @RequestBody —— HTTP 请求体 JSON 自动反序列化为 Java 对象 (依赖 Jackson)
 *   @PathVariable —— URL 路径变量绑定到方法参数
 *   DI 依赖注入 —— @Autowired 自动从 IOC 容器注入 Bean
 *   分页查询 —— 使用 PageHelper 插件, 返回 PageBean 统一封装分页结果
 *   全局统一响应 —— 所有接口返回 Result 包装类, 统一数据格式
 */
@Slf4j
@RequestMapping("/clazzs")
@RestController // @RestController = @Controller + @ResponseBody, 所有方法返回值自动序列化为 JSON
public class ClazzController {

    @Autowired // DI 依赖注入, Spring 自动从 IOC 容器中查找 ClazzService 实例并注入
    private ClazzService clazzService;

    /**
     * 条件分页查询班级列表
     * GET /clazzs?name=xxx&begin=2020-01-01&end=2024-12-31&page=1&pageSize=10
     *
     * 知识点: Spring MVC 数据绑定 —— queryString 参数按名称自动映射到 ClazzQueryParam 对象属性
     *   @DateTimeFormat —— 将请求中的日期字符串 (yyyy-MM-dd) 自动转换为 LocalDate 类型
     *   分页插件 PageHelper: 自动拦截 SQL 生成 count 查询和 limit 分页
     */
    @GetMapping
    public Result page(ClazzQueryParam queryParam) {
        log.info("分页查询班级: {}", queryParam);
        PageBean pageBean = clazzService.page(queryParam);
        return Result.success(pageBean);
    }

    /**
     * 查询全部班级 (用于前端下拉列表)
     * GET /clazzs/list
     *
     * 知识点: 无分页返回所有数据, 适用于数据量小的选项列表场景
     *   常用于新增/修改学员时选择所属班级
     */
    @GetMapping("/list")
    public Result findAll() {
        log.info("查询全部班级");
        List<Clazz> clazzList = clazzService.findAll();
        return Result.success(clazzList);
    }

    /**
     * 新增或修改班级 (根据请求体中是否携带 ID 自动判断)
     * POST /clazzs
     * 请求体JSON: {"name": "班级名称"} 或 {"id": 1, "name": "更新后的班级名称"}
     *
     * 知识点: @RequestBody —— 将 HTTP 请求体中的 JSON 字符串反序列化为 Clazz Java 对象
     *   新增与修改共用同一接口: 无 id 则新增, 有 id 则修改 (符合前后端分离常见设计)
     *   通过 id 是否为 null 区分新增/修改操作
     */
    @PostMapping
    public Result save(@RequestBody Clazz clazz) {
        if (clazz.getId() == null) {
            log.info("新增班级: {}", clazz);
            clazzService.save(clazz);
        } else {
            log.info("修改班级: {}", clazz);
            clazzService.update(clazz);
        }
        return Result.success();
    }

    /**
     * 根据ID查询班级
     * GET /clazzs/{id}
     *
     * 知识点: @PathVariable —— 将 URL 路径中 {id} 占位符的值绑定到方法形参
     *   例: GET /clazzs/5 -> id = 5
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询班级: {}", id);
        Clazz clazz = clazzService.getById(id);
        return Result.success(clazz);
    }

    /**
     * 修改班级 (PUT 方式, 兼容前端 PUT 请求)
     * PUT /clazzs
     * 请求体JSON: {"id": 1, "name": "更新后的班级名称"}
     *
     * 知识点: @PutMapping —— RESTful 规范中 PUT 表示更新资源, 幂等操作 (多次执行结果一致)
     *   PUT 请求必须携带 id 字段标识被更新记录
     */
    @PutMapping
    public Result update(@RequestBody Clazz clazz) {
        if (clazz.getId() == null) {
            return Result.error("班级ID不能为空");
        }
        log.info("修改班级: {}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }

    /**
     * 根据ID删除班级
     * DELETE /clazzs/{id}
     *
     * 知识点: 先校验后删除 —— Service 层检查该班级下是否有关联学员, 有则抛异常阻止删除
     *   异常由 GlobalExceptionHandler 统一捕获, 返回 Result.error(msg) 给前端
     *   @DeleteMapping —— RESTful 规范中 DELETE 表示删除资源
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("根据ID删除班级: {}", id);
        clazzService.delete(id);
        return Result.success();
    }
}
