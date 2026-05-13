package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import com.itheima.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 员工管理 Controller
 * 接口前缀: /emps
 *
 * 知识点:
 *   @Slf4j —— Lombok 日志框架, 编译时生成 log 对象, 用于操作日志记录
 *   @RestController = @Controller + @ResponseBody, 所有方法返回值自动序列化为 JSON
 *   @RequestMapping("/emps") —— 类级别请求路径映射, 所有接口统一前缀
 *   RESTful API 设计: GET 查询 / POST 新增 / PUT 修改 / DELETE 删除
 *   @RequestBody —— JSON 请求体反序列化为 Java 对象
 *   @PathVariable —— URL 路径变量绑定到方法参数
 *   @RequestParam —— 请求参数绑定 (用于非必填 / 批量参数如 List<Integer>)
 *   @RequestHeader —— 请求头参数绑定 (如 token)
 *   DI 依赖注入 —— @Autowired 自动注入 EmpService 和 TokenService
 *   分页查询 —— EmpQueryParam 封装查询条件, PageHelper 插件实现物理分页
 *   全局统一响应 —— 所有接口返回 Result 包装类
 */
@Slf4j
@RequestMapping("/emps")
@RestController
public class EmpController {

    @Autowired
    private EmpService empService;     // 员工业务服务

    @Autowired
    private TokenService tokenService; // Token 管理服务 (用于修改密码时获取当前用户)

    /**
     * 条件分页查询员工列表
     * GET /emps?name=xxx&gender=1&page=1&pageSize=10&begin=2020-01-01&end=2024-12-31
     *
     * 知识点: Spring MVC 自动将 queryString 参数绑定到 EmpQueryParam 对象属性
     *   @DateTimeFormat(pattern = "yyyy-MM-dd") 将日期字符串转为 LocalDate
     *   分页插件 PageHelper: 自动拦截 SQL 生成 count 查询和 limit 分页语句
     */
    @GetMapping
    public Result page(EmpQueryParam queryParam) {
        log.info("分页查询: {}", queryParam);
        PageBean pageBean = empService.page(queryParam);
        return Result.success(pageBean);
    }

    /**
     * 新增员工
     * POST /emps
     * 请求体JSON: 包含员工基本信息 (姓名、性别、手机、邮箱、职位、学历、入职日期、部门ID、工作经验等)
     *
     * 知识点: @RequestBody —— JSON 字符串反序列化为 Java 对象, 支持嵌套对象集合
     *   Jackson 支持嵌套反序列化: 工作经验列表 EmpExpr 自动从 JSON 数组转换
     *   事务管理: Service 层 @Transactional 保证员工基本信息与工作经历同时写入
     */
    @PostMapping
    public Result add(@RequestBody Emp emp) throws Exception {
        log.info("新增员工: {}", emp);
        empService.add(emp);
        return Result.success();
    }

    /**
     * 查询全部员工列表 (用于前端下拉列表选择)
     * GET /emps/list
     *
     * 知识点: 无分页查询, 直接返回所有数据, 适合数据量小的场景
     *   常用于前端下拉选择框的数据来源, 如选择负责人/班主任等
     */
    @GetMapping("/list")
    public Result list() {
        List<Emp> empList = empService.findAll();
        return Result.success(empList);
    }

    /**
     * 批量删除员工
     * DELETE /emps?ids=1,2,3
     *
     * 知识点: @RequestParam List<Integer> —— Spring MVC 自动将 queryString 参数按逗号分隔并转为集合
     *   事务管理: Service 层 @Transactional 保证批量删除的原子性 (全部成功或全部回滚)
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) throws Exception {
        empService.deleteById(ids);
        return Result.success();
    }

    /**
     * 根据ID查询员工信息 (用于编辑时数据回显)
     * GET /emps/{id}
     *
     * 知识点: @PathVariable —— URL 路径变量 {id} 绑定到方法参数
     *   关联查询: Service 层级联查询员工基本信息及其关联的工作经历列表
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    /**
     * 更新员工信息
     * PUT /emps
     * 请求体JSON: 包含员工完整信息和ID (含工作经验列表)
     *
     * 知识点: @PutMapping —— RESTful 规范中 PUT 表示更新资源, 幂等操作
     *   先删后增策略: Service 层先删除原有工作经历, 再批量插入新的工作经历
     */
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        empService.update(emp);
        return Result.success();
    }

    /**
     * 修改当前登录员工密码
     * PUT /emps/changePassword
     * 请求体JSON: {"password": "原密码", "newpassword": "新密码"}
     * 请求头: token (用于获取当前登录用户身份)
     *
     * 知识点:
     *   @RequestHeader("token") —— 从 HTTP 请求头中获取指定头部值
     *   TokenService —— 根据 token 解析当前登录员工身份
     *   安全性: 先验证原密码正确性, 校验通过后才更新为新密码
     *   Map<String, String> 接收 —— 适用于 JSON 结构简单的动态请求体
     */
    @PutMapping("/changePassword")
    public Result changePassword(@RequestBody Map<String, String> body, @RequestHeader("token") String token) {
        // 根据 token 获取当前登录用户信息
        Emp loginEmp = tokenService.getEmpByToken(token);
        if (loginEmp == null) {
            return Result.error("请先登录");
        }

        // 校验原密码是否正确
        String oldPassword = body.get("password");
        if (!oldPassword.equals(loginEmp.getPassword())) {
            return Result.error("原密码错误");
        }

        // 更新为新密码
        Emp emp = new Emp();
        emp.setId(loginEmp.getId());
        emp.setPassword(body.get("newpassword"));
        empService.changePassword(emp);
        return Result.success();
    }
}
