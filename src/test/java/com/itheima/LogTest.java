package com.itheima;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

    //定义日志记录对象 - 固定操作
    private static final Logger log = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void testLog(){
        log.info("开始计算...");

        int sum = 0;
        try {
            int[] nums = {1, 5, 3, 2, 1, 4, 5, 4, 6, 7, 4, 34, 2, 23};
            for (int i = 0; i < nums.length; i++) {
                sum += nums[i];
            }
        } catch (Exception e) {
            log.info("程序运行出错...");
        }

        log.info("计算结果为: "+sum);
        log.info("结束计算...");
    }


    @Test
    public void testLogLevel(){
        log.trace("trace message .... ");
        log.debug("debug message ....");
        log.info("info message ....");
        log.warn("warn message ....");
        log.error("error message ....");
    }

}
