package com.itheima.service;

import com.itheima.pojo.Emp;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token 管理 Service (基于本地内存)
 *
 * 知识点:
 *   @Service —— Spring IOC 容器管理, 单例 Bean (默认 scope=singleton)
 *   ConcurrentHashMap —— 线程安全的 HashMap, 适用于高并发场景 (读写分离锁)
 *   本地内存缓存 —— Token 存储在 JVM 内存中, 服务重启后丢失, 适用于单机部署
 *   生产环境改进 —— 建议替换为 Redis 等集中式缓存, 实现分布式 Session 共享
 */
@Service
public class TokenService {

    /**
     * Token 存储: key=Token字符串, value=登录用户信息
     * 知识点: ConcurrentHashMap —— key 和 value 均不能为 null
     */
    private final Map<String, Emp> tokenStore = new ConcurrentHashMap<>();

    /**
     * 创建 Token 并关联登录用户
     * 知识点: UUID.randomUUID() 生成全局唯一标识, replace("-","") 去掉横线使其更简洁
     * @param emp 登录用户信息
     * @return 生成的 Token 字符串
     */
    public String createToken(Emp emp) {
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenStore.put(token, emp);
        return token;
    }

    /**
     * 根据 Token 获取登录用户信息
     * 知识点: Map.get() 时间复杂度 O(1), 通过 Token 反查用户, 用于拦截器中的登录校验
     * @param token Token 字符串
     * @return 用户信息, Token 不存在或已过期返回 null
     */
    public Emp getEmpByToken(String token) {
        return tokenStore.get(token);
    }

    /**
     * 移除 Token (退出登录时调用)
     * 知识点: 清除映射关系, 后续请求携带该 Token 将无法通过认证, 实现退出效果
     * @param token 要移除的 Token
     */
    public void removeToken(String token) {
        tokenStore.remove(token);
    }
}
