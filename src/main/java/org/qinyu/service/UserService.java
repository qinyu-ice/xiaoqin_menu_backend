package org.qinyu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.dto.UserLoginDTO;
import org.qinyu.dto.UserRegisterDTO;
import org.qinyu.dto.UserResetPasswordDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    String add(UserRegisterDTO dto);

    Map<String, Object> login(UserLoginDTO dto);

    String update(UserUpdateDTO dto, String token);

    String delete(List<String> ids);

    String applyResetPassword(String id);

    String resetPassword(List<UserResetPasswordDTO> dtoList);

    User getUserInfo(String id, String token);

    String editPermission(String id, Integer permission,String token);
}
