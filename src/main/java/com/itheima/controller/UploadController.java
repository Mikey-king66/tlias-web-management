package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.util.AliyunOSSProperties;
import com.itheima.util.AliyunOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传的Controller
 */
@Slf4j
@RestController
public class UploadController {

//    @Value("${aliyun.oss.endpoint}")
//    private String endpoint;
//    @Value("${aliyun.oss.bucketName}")
//    private String bucketName;

    @Autowired
    private AliyunOSSProperties ossProperties;


    /**
     * 本地存储 - 存储在服务器本地磁盘目录
     */
    /*@PostMapping("/upload")
    public Result upload(String username, Integer age, MultipartFile file) throws Exception {
        log.info("文件上传, username: {}, age:{}, file: {}", username , age, file);

        //将文件存储起来
        //获取原始文件名.
        String originalFilename = file.getOriginalFilename(); //中国梦.txt
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")); //.txt

        //构建一个新的文件名 - 不能重复
        String newFileName = UUID.randomUUID().toString() + extName;

        //文件存储
        file.transferTo(new File("D:/images/" + newFileName));

        return Result.success();
    }*/


    /**
     * 阿里云OSS
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传 , 上传的文件名: {}", file.getOriginalFilename());
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String url = AliyunOSSUtils.upload(ossProperties.getEndpoint(), ossProperties.getBucketName(), file.getBytes(), extName);
        return Result.success(url);
    }

}
