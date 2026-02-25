package org.practice.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.practice.common.entity.Permission;


@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
