package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.dto.UserLoginDTO;
import org.qinyu.dto.UserRegisterDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {

    String add(UserRegisterDTO dto);

    Map<String, Object> login(UserLoginDTO dto);

    String update(UserUpdateDTO dto);
}
