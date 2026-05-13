package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import com.itheima.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 登录认证 Controller
 * 接口路径: /login
 *
 * 知识点:
 *   @Slf4j —— Lombok 日志框架, 用于记录登录操作日志
 *   @RestController —— 所有方法返回 JSON 格式数据
 *   无 @RequestMapping —— 此 Controller 不设类级别路径前缀, 方法级别直接指定完整路径
 *   Token 认证: 登录成功后生成 Token 返给前端, 后续请求携带 Token 进行身份认证
 *   无状态认证: 服务端不保存 Session, 通过 Token 中的用户信息鉴权
 *   @RequestBody Map<String, String> —— 接收 JSON 格式简单的登录请求体
 *   DI 依赖注入 —— @Autowired 自动注入 EmpService 和 TokenService
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private EmpService empService;      // 员工业务服务 (查询用户)

    @Autowired
    private TokenService tokenService;  // Token 管理服务 (生成/验证 Token)

    /**
     * 用户登录认证
     * POST /login
     * 请求体JSON: {"username": "用户名", "password": "密码"}
     *
     * 知识点:
     *   @RequestBody Map<String, String> —— 接收 JSON 格式请求体
     *     适用于结构简单、无需定义实体类的场景
     *   Token 生成: TokenService.createToken(emp) 创建 JWT 令牌并返回
     *   安全性说明: 当前使用明文密码比对 (生产环境应使用 BCrypt 等加密算法)
     *   Map.of() —— Java 9+ 提供的不可变 Map 工厂方法, 便捷构造返回数据
     *
     * @return 登录成功返回用户信息 (id / token / name / username)
     *         登录失败返回错误信息 "用户名或密码错误"
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        // 根据用户名查询用户信息
        Emp emp = empService.getByUsername(username);
        // 校验用户名是否存在以及密码是否匹配
        if (emp == null || !password.equals(emp.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        // 生成 Token 并关联当前登录用户
        String token = tokenService.createToken(emp);

        // 构造返回数据 (仅返回必要信息, 不返回密码等敏感字段)
        Map<String, Object> data = Map.of(
            "id", emp.getId(),
            "token", token,
            "name", emp.getName(),
            "username", emp.getUsername()
        );

        return Result.success(data);
    }
}
