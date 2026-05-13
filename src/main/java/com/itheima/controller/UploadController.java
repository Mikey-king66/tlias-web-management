package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.util.AliyunOSSProperties;
import com.itheima.util.AliyunOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传 Controller
 * 接口路径: /upload
 *
 * 知识点:
 *   @Slf4j —— Lombok 日志框架, 记录文件上传操作日志
 *   @RestController —— 返回 JSON 格式响应
 *   MultipartFile —— Spring MVC 封装上传文件的核心接口
 *     常用方法: getOriginalFilename() 获取原始文件名, getBytes() 获取文件字节数组
 *   文件上传配置: 在 application.yml 中配置 multipart 文件大小限制和编码
 *   阿里云 OSS: 对象存储服务, 提供高可用、高安全的云存储方案
 *   配置文件注入: @ConfigurationProperties 读取配置项 (比 @Value 更结构化)
 *   form-data 请求: 前端使用 multipart/form-data 格式上传文件
 */
@Slf4j
@RestController
public class UploadController {

    /**
     * 阿里云 OSS 配置属性
     * 知识点: @ConfigurationProperties(prefix = "aliyun.oss") 将 yml 配置自动绑定到对象
     *   对比 @Value 逐个注入: @ConfigurationProperties 更简洁, 适合多个相关配置项
     */
    @Autowired
    private AliyunOSSProperties ossProperties;

    /**
     * 文件上传 (阿里云 OSS 云存储)
     * POST /upload
     * 请求格式: multipart/form-data (enctype="multipart/form-data")
     *
     * 知识点:
     *   MultipartFile —— Spring MVC 文件上传核心接口
     *   阿里云 OSS 上传流程: 创建 OSSClient -> 生成唯一不重复文件名 -> 上传文件字节 -> 返回文件 URL
     *   唯一文件名策略: UUID + 原文件扩展名, 防止文件名冲突
     *   AliyunOSSUtils.upload() —— 封装了 OSS SDK 的上传调用逻辑
     *   返回 URL: 前端可直接使用该 URL 展示或下载文件
     */
    /* 本地存储方式 - 存储在服务器本地磁盘目录
    @PostMapping("/upload")
    public Result upload(String username, Integer age, MultipartFile file) throws Exception {
        log.info("文件上传, username: {}, age:{}, file: {}", username , age, file);
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + extName;
        file.transferTo(new File("D:/images/" + newFileName));
        return Result.success();
    }*/

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传 , 上传的文件名: {}", file.getOriginalFilename());
        // 获取原始文件的扩展名 (如 .jpg, .png)
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 调用阿里云 OSS 工具类上传文件并获取访问 URL
        String url = AliyunOSSUtils.upload(ossProperties.getEndpoint(), ossProperties.getBucketName(), file.getBytes(), extName);
        return Result.success(url);
    }

}
