package org.practice.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.practice.common.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

     User findByUsername(String username);
}
