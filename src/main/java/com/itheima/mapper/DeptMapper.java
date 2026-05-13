package com.itheima.mapper;

import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 部门管理的Mapper接口 -
 */
@Mapper
public interface DeptMapper {

    /**
     * 查询所有部门数据
     */
    //方式一 :
    //@Results({
    //        @Result(column = "create_time", property = "createTime"),
    //        @Result(column = "update_time", property = "updateTime")
    //})

    //方式二 : 起别名
    //@Select("select id, name, create_time createTime, update_time updateTime from dept")

    @Select("select id, name, create_time, update_time from dept")
    public List<Dept> findAll();

    /**
     * 根据ID删除部门
     */
    @Delete("delete from dept where id = #{id}")
    void deleteById(Integer id);

    /**
     * 添加部门
     */
    @Insert("insert into dept(name, create_time, update_time) values (#{name},#{createTime},#{updateTime})")
    void insert(Dept dept);

    /**
     * 根据ID查询部门
     */
    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getById(Integer id);

    /**
     * 根据ID更新部门数据
     */
    //@Update("update dept set name = #{name} , update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);
}
