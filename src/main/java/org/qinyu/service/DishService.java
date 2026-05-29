package org.qinyu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.qinyu.entity.Dish;

public interface DishService extends IService<Dish> {

    String add(Dish dish, String token);

    Page<Dish> getByPage(Integer page, Integer pageSize, String name);
}
