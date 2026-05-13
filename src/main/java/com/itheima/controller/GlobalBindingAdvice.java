package com.itheima.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * 全局数据绑定增强器
 *
 * 知识点:
 *   @ControllerAdvice —— Spring MVC 全局控制器增强注解
 *     可指定 basePackages/basePackageClasses 限定作用范围
 *     三种增强类型: @InitBinder / @ModelAttribute / @ExceptionHandler
 *   @InitBinder —— 初始化数据绑定器, 对所有请求参数进行统一预处理
 *   StringTrimmerEditor —— Spring 提供的字符串裁剪编辑器
 *     true: 将空字符串 "" 转换为 null (避免空字符串存入数据库)
 *     false: 仅去除前后空格, 保留空字符串
 *
 * 作用:
 *   1. 解决前端传空字符串时, 数据库 null 字段校验不通过的问题
 *   2. 避免脏数据: 去除用户意外输入的前后空格, 保证数据质量
 *   3. 实际效果: 用户输入 "  张三  " -> "张三", 用户输入 "" -> null
 */
@ControllerAdvice
public class GlobalBindingAdvice {

    /**
     * 初始化 Web 数据绑定器, 对所有 String 类型请求参数进行预处理
     *
     * 知识点:
     *   WebDataBinder —— Spring MVC 请求参数绑定核心组件
     *   PropertyEditor —— JavaBean 属性编辑器机制 (java.beans.PropertyEditorSupport)
     *   registerCustomEditor —— 注册自定义属性编辑器, 对指定类型统一生效
     *
     * @param binder WebDataBinder —— Spring MVC 数据绑定器, 负责将请求参数绑定到 Java 对象
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 注册字符串裁剪编辑器: 所有 String 参数自动 trim(), 空串转 null
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
