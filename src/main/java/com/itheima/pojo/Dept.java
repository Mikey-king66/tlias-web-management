package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor //无参构造
@AllArgsConstructor //全参构造
public class Dept {
    private Integer id; //推荐使用包装类型 (基本类型有默认值)
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
