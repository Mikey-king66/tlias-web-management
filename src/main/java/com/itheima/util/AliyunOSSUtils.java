package com.itheima.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.util.UUID;

/**
 * 阿里云 OSS 操作工具类
 *
 * 知识点:
 *   阿里云 OSS SDK —— 使用 OSSClientBuilder 创建 OSS 客户端, 通过 PutObjectRequest 上传文件
 *   EnvironmentVariableCredentialsProvider —— 从环境变量 OSS_ACCESS_KEY_ID 和
 *     OSS_ACCESS_KEY_SECRET 中读取阿里云访问凭证, 避免将密钥硬编码在代码中
 *   UUID —— java.util.UUID.randomUUID() 生成全局唯一的文件名, 防止文件覆盖和命名冲突
 *   文件上传流程: 构建 PutObjectRequest → ossClient.putObject() 执行上传 → finally 中关闭客户端
 *   OSSException / ClientException —— SDK 定义的两种异常:
 *     OSSException: 服务端拒绝请求 (如权限不足、Bucket 不存在)
 *     ClientException: 客户端内部错误 (如网络不可达)
 *   @Slf4j (Lombok) —— 自动生成 log 日志对象, 用于记录异常信息
 */
@Slf4j
public class AliyunOSSUtils {

    /**
     * 上传文件到阿里云 OSS
     *
     * 流程说明:
     *   1. 通过 EnvironmentVariableCredentialsProvider 从环境变量获取阿里云访问凭证
     *   2. 使用 UUID.randomUUID() 生成唯一文件名, 避免同名文件覆盖
     *   3. 创建 OSS 客户端并执行 putObject 请求完成上传
     *   4. 拼接并返回文件的公开访问 URL (格式: https://bucketName.endpoint/objectName)
     *
     * @param endpoint   OSS 地域节点域名 (如 oss-cn-beijing.aliyuncs.com)
     * @param bucketName 存储空间 (Bucket) 名称
     * @param content    文件内容的字节数组
     * @param extName    文件扩展名 (如 .jpg, .png), 用于生成完整文件名
     * @return 文件上传后的公开可访问 URL 地址
     * @throws Exception 上传过程中可能抛出的各类异常
     */
    public static String upload(String endpoint, String bucketName, byte[] content, String extName) throws Exception {
        // 从环境变量中获取访问凭证。运行本代码示例之前, 请确保已设置环境变量 OSS_ACCESS_KEY_ID 和 OSS_ACCESS_KEY_SECRET
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 生成唯一文件名: UUID + 扩展名 (如 a1b2c3d4.jpg)
        String objectName = UUID.randomUUID() + extName;

        // 创建 OSSClient 实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
        try {
            // 创建 PutObjectRequest 对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(content));
            // 执行上传请求
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            // OSS 异常: 请求到达 OSS 但被拒绝 (如权限问题)
            log.error("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            // 客户端异常: 本地网络或配置问题
            log.error("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, such as not being able to access the network.");
            log.error("Error Message:" + ce.getMessage());
        } finally {
            // 无论上传成功与否都要关闭客户端, 释放连接资源
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        // 拼接文件访问 URL: https://bucketName.endpoint/objectName
        return endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + objectName;
    }

}
