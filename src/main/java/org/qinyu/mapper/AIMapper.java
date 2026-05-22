package org.qinyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.qinyu.entity.Conversation;

@Mapper
public interface AIMapper extends BaseMapper<Conversation> {
}
