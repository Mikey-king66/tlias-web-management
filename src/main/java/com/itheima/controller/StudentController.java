package com.itheima.controller;

import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学员管理 Controller
 * 接口前缀: /students
 *
 * 知识点:
 *   @Slf4j —— Lombok 日志框架, 编译时生成 log 对象, 用于记录操作日志
 *   @RestController = @Controller + @ResponseBody, 所有方法返回值自动序列化为 JSON
 *   @RequestMapping("/students") —— 类级别请求路径映射, 统一接口前缀
 *   RESTful API 设计: GET 查询, POST 新增, PUT 修改, DELETE 删除
 *   @PathVariable —— 路径参数绑定, 如 /students/{id} 中的 id
 *   @RequestBody —— JSON 请求体自动反序列化为 Java 对象 (依赖 Jackson)
 *   DI 依赖注入 —— @Autowired 自动注入 StudentService
 *   分页查询 —— StudentQueryParam 封装查询条件, PageHelper 实现物理分页
 *   全局统一响应 —— 所有接口返回 Result 包装类
 */
@Slf4j
@RequestMapping("/students")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 条件分页查询学员列表
     * GET /students?name=张三&degree=1&clazzId=2&page=1&pageSize=10
     *
     * 知识点: Spring MVC 数据绑定 —— queryString 参数自动映射到 StudentQueryParam 对象属性
     *   分页插件 PageHelper: 自动拦截 SQL 拼接 count 查询和 limit 分页
     *   多条件筛选: 支持按姓名、学历、班级等多个条件组合查询
     */
    @GetMapping
    public Result page(StudentQueryParam queryParam) {
        log.info("分页查询学员: {}", queryParam);
        PageBean pageBean = studentService.page(queryParam);
        return Result.success(pageBean);
    }

    /**
     * 根据ID查询学员信息 (用于编辑时数据回显)
     * GET /students/{id}
     *
     * 知识点: @PathVariable —— 将 URL 路径变量 {id} 绑定到方法参数
     *   RESTful 风格: 资源 ID 放在 URL 路径中
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询学员: {}", id);
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    /**
     * 新增学员
     * POST /students
     * 请求体JSON: 包含学员完整信息 (姓名、手机、学历、班级ID 等)
     *
     * 知识点: @RequestBody —— 将请求体 JSON 转为 Student 对象 (依赖 Jackson 反序列化)
     *   前端必须设置 Content-Type: application/json
     */
    @PostMapping
    public Result save(@RequestBody Student student) {
        log.info("新增学员: {}", student);
        studentService.save(student);
        return Result.success();
    }

    /**
     * 修改学员信息
     * PUT /students
     * 请求体JSON: 包含学员完整信息和 ID
     *
     * 知识点: @PutMapping —— RESTful 规范中 PUT 用于更新操作, 幂等 (多次执行结果一致)
     *   @RequestBody —— 请求体 JSON 反序列化为 Java 对象
     */
    @PutMapping
    public Result update(@RequestBody Student student) {
        log.info("修改学员: {}", student);
        studentService.update(student);
        return Result.success();
    }

    /**
     * 批量删除学员
     * DELETE /students/{ids}
     *
     * 知识点: @PathVariable 结合 List 类型 —— 自动将 "1,2,3" 路径转换为 [1, 2, 3] 集合
     *   批量操作: 事务管理保证原子性, 全部删除或全部回滚
     */
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        log.info("批量删除学员: {}", ids);
        studentService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 违纪处理: 扣分
     * PUT /students/violation/{id}/{score}
     *
     * 知识点: @PathVariable —— 多个路径参数按名称分别绑定到不同方法形参
     *   例: PUT /students/violation/7/5 -> id=7 (学员ID), score=5 (扣分数值)
     *   数据库原子操作: violation_count + 1, violation_score 累加, update_time 刷新
     *   非幂等操作: 每次请求累加扣分, 需前端防止重复提交
     */
    @PutMapping("/violation/{id}/{score}")
    public Result violation(@PathVariable Integer id, @PathVariable Integer score) {
        log.info("违纪处理: 学员ID={}, 扣{}分", id, score);
        studentService.violation(id, score);
        return Result.success();
    }
}
