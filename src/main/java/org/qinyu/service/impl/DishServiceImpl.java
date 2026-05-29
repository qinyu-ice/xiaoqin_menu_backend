package org.qinyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.qinyu.entity.Dish;
import org.qinyu.entity.User;
import org.qinyu.mapper.DishMapper;
import org.qinyu.mapper.UserMapper;
import org.qinyu.service.DishService;
import org.qinyu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String add(Dish dish, String token) {
        String currentId = jwtUtil.getUserIdFromToken(token);
        User user = userMapper.selectById(currentId);
        if (user.getPermission() == 3) {
            return "暂无权限";
        }
        if (dish == null) {
            return "菜品新增信息不能为空";
        }
        if (dishMapper.isExist(dish.getName())) {
            return "菜品" + dish.getName() + "已存在";
        }
        dish.setId("dish" + UUID.randomUUID().toString().replace("-", "").substring(0, 22));
        dish.setAuthor(user.getName());
        dishMapper.add(dish);
        return "菜品" + dish.getName() + "新增成功";
    }

    @Override
    public Page<Dish> getByPage(Integer page, Integer pageSize, String name) {
        // 1. 创建分页对象
        Page<Dish> pageParam = Page.of(page, pageSize);

        // 2. 构建查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 如果name参数不为空且不是空字符串，添加模糊查询条件
        if (name != null && !name.trim().isEmpty()) {
            queryWrapper.like(Dish::getName, name.trim()).orderByDesc(Dish::getCreateTime);
        }

        return page(pageParam, queryWrapper);
    }
}
