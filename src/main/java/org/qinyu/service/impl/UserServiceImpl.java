package org.qinyu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinyu.dto.UserLoginDTO;
import org.qinyu.dto.UserRegisterDTO;
import org.qinyu.dto.UserResetPasswordDTO;
import org.qinyu.dto.UserUpdateDTO;
import org.qinyu.entity.User;
import org.qinyu.mapper.UserMapper;
import org.qinyu.service.UserService;
import org.qinyu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate redisTemplate;

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
        user.setIsEnable(1);
        user.setIsResetPassword(1);
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
        if (user.getIsEnable() == 0) {
            map.put("message", "用户" + dto.getName() + "已被禁用");
            return map;
        }
        if (user.getIsResetPassword() == 2) {
            map.put("message", "用户" + dto.getName() + "重置密码中，暂时无法登录");
            return map;
        }
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            map.put("message", "用户" + dto.getName() + "密码错误");
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
        map.put("name", user.getName());
        map.put("avatar", user.getAvatar());
        map.put("token", jwt);
        String tokenKey = "user:token:" + user.getId();
        String refreshKey = "user:refresh:" + user.getId();
        redisTemplate.opsForValue().set(tokenKey, jwt, Duration.ofHours(2));
        redisTemplate.opsForValue().set(refreshKey, "refresh" + jwt, Duration.ofDays(7));
        return map;
    }

    @Override
    public String update(UserUpdateDTO dto, String token) {
        User user = getUserByToken(token);
        // 任何人都可修改自己的信息
        if (user.getId().equals(dto.getId())) {
            userMapper.updateById(dto);
            return "用户" + dto.getId() + "更新成功";
        }
        // 超级管理员可以修改任何人的信息
        if (user.getPermission() == 1) {
            userMapper.updateById(dto);
            return "用户" + dto.getId() + "更新成功";
        }
        // 普通管理员可修改普通用户的信息
        if (user.getPermission() == 2 && userMapper.selectById(dto.getId()).getPermission() == 3) {
            userMapper.updateById(dto);
            return "用户" + dto.getId() + "更新成功";
        }
        throw new RuntimeException("暂无权限");
    }

    @Override
    public String delete(List<String> ids) {
        for (String id : ids) {
            if (id.isEmpty()) {
                return "用户ID不能为空";
            }
        }
        userMapper.deleteByIds(ids);
        return "用户删除成功";
    }

    @Override
    public String applyResetPassword(String name) {
        if (name.isEmpty()) {
            return "用户名不能为空";
        }
        boolean exists = userMapper.getUserByName(name) > 0;
        if (!exists) {
            return "用户名不存在";
        }
        userMapper.updateIsResetPassword(name);
        return "用户" + name + "申请重置密码成功，请等待管理员审核";
    }

    @Override
    public String resetPassword(List<UserResetPasswordDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return "用户密码重置列表不能为空";
        }
        for (UserResetPasswordDTO dto : dtoList) {
            if (dto.getId().isEmpty()) {
                return "用户ID不能为空";
            }
            // 明文密码加密
            dto.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        }
        userMapper.resetPassword(dtoList);
        return "用户重置密码成功";
    }

    @Override
    public User getUserInfo(String id, String token) {
        User user = getUserByToken(token);
        // 任何人都可查看自己的信息
        if (user.getId().equals(id)) {
            return userMapper.selectById(id);
        }
        // 超级管理员可以查看任何人的信息
        if (user.getPermission() == 1) {
            return userMapper.selectById(id);
        }
        // 普通管理员可查看普通用户的信息
        if (user.getPermission() == 2 && userMapper.selectById(id).getPermission() == 3) {
            return userMapper.selectById(id);
        }
        throw new RuntimeException("暂无权限");
    }

    @Override
    public String editPermission(String id, Integer permission, String token) {
        User user = getUserByToken(token);
        if (user.getPermission() == 1) {
            userMapper.editPermission(id, permission);
        }
        return "修改用户权限成功";
    }

    @Override
    public Boolean exit(String id) {
        if (id.isEmpty()) {
            return false;
        }
        String tokenKey = "user:token:" + id;
        String refreshKey = "user:refresh:" + id;
        redisTemplate.delete(tokenKey);
        redisTemplate.delete(refreshKey);
        return true;
    }

    // 获取当前登录用户信息
    private User getUserByToken(String token) {
        String currentUserId = jwtUtil.getUserIdFromToken(token);
        return lambdaQuery().eq(User::getId, currentUserId).one();
    }
}
