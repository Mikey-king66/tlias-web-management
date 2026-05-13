package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Tlias 后台管理系统 —— Spring Boot 应用入口 (启动类 / 引导类)
 *
 * 知识点:
 *   @SpringBootApplication —— 组合注解, 包含:
 *     - @SpringBootConfiguration: 标识当前类为配置类
 *     - @EnableAutoConfiguration: 开启自动配置, 根据依赖的 jar 包自动装配 (内嵌 Tomcat、数据源等)
 *     - @ComponentScan: 默认扫描启动类所在包及其子包, 将 @Component / @Service / @Controller 等注册为 Bean
 *   SpringApplication.run() —— 启动 Spring Boot 应用, 初始化 IoC 容器并启动内嵌 Tomcat
 */
@SpringBootApplication // 封装 @ComponentScan, 具备组件扫描功能, 扫描引导类所在包及其子包
public class TliasWebManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TliasWebManagementApplication.class, args);
    }

}
