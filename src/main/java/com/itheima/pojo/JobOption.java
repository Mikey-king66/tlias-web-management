package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 员工职位人数统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOption {
    private List jobList; //职位列表
    private List dataList; //人数列表
}
