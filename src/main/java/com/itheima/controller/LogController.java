package com.itheima.controller;

import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志 Controller
 * 接口前缀: /log
 *
 * 知识点:
 *   @Slf4j —— Lombok 日志框架, 编译时生成 log 对象, 用于记录操作日志
 *   @RestController = @Controller + @ResponseBody, 所有方法返回 JSON 格式数据
 *   @RequestMapping("/log") —— 类级别请求路径映射, 统一接口前缀
 *   操作日志 —— 记录用户在系统中的关键操作 (登录、增删改等), 用于安全审计和操作追溯
 *   AOP 记录日志: 通常通过 Spring AOP @Around 环绕通知统一拦截并记录操作日志
 *   分页查询: 操作日志数据量大, 必须使用分页查询避免性能问题
 *   DI 依赖注入 —— @Autowired 自动注入 LogService
 */
@Slf4j
@RequestMapping("/log")
@RestController
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询操作日志
     * GET /log/page?page=1&pageSize=15
     *
     * 知识点:
     *   @RequestParam(defaultValue = "1") —— 设置请求参数的默认值
     *   操作日志通常包含: 操作人、操作时间、操作类型 (增/删/改/查)、操作描述等
     *   日志数据量随系统使用逐渐增大, 必须分页展示避免前端渲染性能问题
     *   手动分页: LIMIT start, pageSize (未使用 PageHelper 插件)
     *
     * @param page 当前页码, 从1开始, 默认值1
     * @param pageSize 每页显示条数, 默认值15
     */
    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "15") Integer pageSize) {
        PageBean pageBean = logService.page(page, pageSize);
        return Result.success(pageBean);
    }
}
