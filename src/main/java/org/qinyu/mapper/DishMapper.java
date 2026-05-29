package org.qinyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.qinyu.entity.Dish;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Insert("insert into dish (id, name,introduction, img, kind, faction, seasoning, food_material, procedure_step,note,author, create_time, update_time) " +
            "values (#{id},#{name},#{introduction},#{img},#{kind},#{faction},#{seasoning},#{foodMaterial},#{procedureStep},#{note},#{author},now(),now())")
    void add(Dish dish);

    @Select("select count(*) > 0 from dish where name=#{name}")
    Boolean isExist(String name);
}
