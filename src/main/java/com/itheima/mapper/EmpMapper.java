package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
    /**
     * 获取总记录数
     */
    //@Select("select count(*) from emp left join dept on emp.dept_id = dept.id")
    //Long count();

    /**
     * 分页查询
     */
    //@Select("select emp.*, dept.name deptName from emp left join dept on emp.dept_id = dept.id limit #{start},#{pageSize}")
    //List<Emp> list(Integer start, Integer pageSize);



    /**
     * 员工查询
     */
    //@Select("select emp.*, dept.name deptName from emp left join dept on emp.dept_id = dept.id")
    List<Emp> list(EmpQueryParam queryParam);

    /**
     * 插入员工数据
     */
    @Options(useGeneratedKeys = true, keyProperty = "id") //需要获取数据库自动增长的主键, 并赋值给emp对象的id属性
    @Insert("insert into emp (username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) values " +
            "(#{username}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);

    void deleteByIds(List<Integer> ids);


    Emp getById(Integer id);

    void updateById(Emp emp);

    List<Map<String, Object>> countEmpJobData();

    // @MapKey("name")
    List<Map> countEmpGenderData();
}
