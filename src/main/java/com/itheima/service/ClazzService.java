package com.itheima.service;

import com.itheima.pojo.Clazz;
import java.util.List;

public interface ClazzService {

    /**
     * 添加班级信息
     * @param clazz
     */
    void save(Clazz clazz);

    /**
     * 查询全部班级
     * @return
     */
    List<Clazz> findAll();
}
