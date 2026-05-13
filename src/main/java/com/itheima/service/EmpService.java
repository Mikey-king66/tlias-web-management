package com.itheima.service;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageBean;

import java.util.List;

public interface EmpService {
    /**
     * 分页条件查询
     */
    PageBean page(EmpQueryParam queryParam);

    /**
     * 新增员工
     * @param emp
     */
    void add(Emp emp) throws Exception;

    void deleteById(List<Integer> ids);

    Emp getInfo(Integer id);

    void update(Emp emp);
}
