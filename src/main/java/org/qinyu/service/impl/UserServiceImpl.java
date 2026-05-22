package org.qinyu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinyu.dto.UserLoginDTO;
import org.qinyu.dto.UserRegisterDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;
import org.qinyu.mapper.UserMapper;
import org.qinyu.service.UserService;
import org.qinyu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String add(UserRegisterDTO dto) {
        if (dto == null) {
            return "用户注册信息不能为空";
        }
        if (!Objects.equals(dto.getPassword(), dto.getRePassword())) {
            return "用户输入的两次密码不一致";
        }
        if (lambdaQuery().eq(User::getName, dto.getName()).last("limit 1").count() > 0) {
            return "用户名" + dto.getName() + "已存在";
        }
        User user = new User();
        // 随机生成26位UUID赋值给用户ID
        user.setId(UUID.randomUUID().toString().replace("-", "").substring(0, 26));
        user.setNickname(dto.getName());
        user.setName(dto.getName());
        // SpringSecurity BCrypt 加密
        // 校验方法：BCrypt.checkpw(明文, 数据库密文)
        user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        user.setPermission(3);
        userMapper.add(user);
        return "用户" + dto.getName() + "注册成功";
    }

    @Override
    public Map<String, Object> login(UserLoginDTO dto) {
        Map<String, Object> map = new HashMap<>();
        if (dto == null) {
            map.put("message", "用户登录信息不能为空");
            return map;
        }
        User user = lambdaQuery().eq(User::getName, dto.getName()).one();
        if (Objects.isNull(user)) {
            map.put("message", "用户" + dto.getName() + "不存在");
            return map;
        }
        // 创建Token
        // 1. 创建未认证的 Authentication 对象
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.getName(), dto.getPassword());

        // 2. 调用 AuthenticationManager 进行认证（会触发 UserDetailsService + PasswordEncoder）
        Authentication authentication = authenticationManager.authenticate(authToken);

        // 3. 认证成功后生成 JWT
        String jwt = jwtUtil.generateToken(dto.getName(), user.getId());

        map.put("message", "用户" + dto.getName() + "登录成功");
        map.put("id", user.getId());
        map.put("token", jwt);
        return map;
    }

    @Override
    public String update(UserUpdateDTO dto) {
        if (dto.getId().isEmpty()) {
            return "用户ID为空";
        }
        userMapper.updateById(dto);
        return "用户" + dto.getId() + "更新成功";
    }
}
