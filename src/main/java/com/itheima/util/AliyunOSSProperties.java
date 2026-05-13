package com.itheima.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 配置属性类
 *
 * 知识点:
 *   @ConfigurationProperties(prefix = "aliyun.oss") —— Spring Boot 属性绑定注解
 *     将 application.yml 中以 "aliyun.oss" 为前缀的配置项自动注入到同名字段
 *     如: aliyun.oss.endpoint -> endpoint, aliyun.oss.bucketName -> bucketName
 *   @Component —— 将本类注册为 Spring Bean, 使其可以被其他组件注入使用
 *   @Data (Lombok) —— 自动生成 getter/setter/toString/equals/hashCode 等模板方法
 *
 * 属性绑定的底层原理: Spring Boot 通过 setter 方法注入配置值, 字段名需与配置项后缀匹配
 *
 * 对比 @Value 方式:
 *   @Value("${aliyun.oss.endpoint}") 逐个注入 —— 代码冗长
 *   @ConfigurationProperties 聚合注入 —— 更简洁, 类型安全, 支持松散绑定
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOSSProperties {
    private String endpoint;   // OSS 地域节点域名 (如 oss-cn-beijing.aliyuncs.com)
    private String bucketName; // 存储空间 (Bucket) 名称
}
