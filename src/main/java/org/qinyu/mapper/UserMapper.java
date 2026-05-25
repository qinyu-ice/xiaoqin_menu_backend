package org.qinyu.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.qinyu.dto.UserResetPasswordDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 使用 MyBatis-Plus 自带的方法即可，也可以自定义按用户名查询的方法
    default User findByUsername(String name) {
        return selectOne(new LambdaQueryWrapper<User>().eq(User::getName, name));
    }

    @Insert("insert into user (id,nickname, name, password, permission,is_enable,is_reset_password, create_time, update_time) " +
            "VALUES (#{id},#{nickname},#{name},#{password},#{permission},#{isEnable},#{isResetPassword},now(),now())")
    void add(User user);

    void updateById(UserUpdateDTO dto);

    void deleteByIds(List<String> ids);

    @Update("update user set is_reset_password = 2 where id = #{id}")
    void updateIsResetPassword(String id);

    void resetPassword(List<UserResetPasswordDTO> dtoList);

    @Update("update user set permission = #{permission} where id = #{id}")
    void editPermission(String id, Integer permission);
}
