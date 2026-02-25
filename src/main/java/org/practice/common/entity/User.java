package org.practice.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("users")
public class User {
    private Long id;
    private String username;
    private String password;
    private Integer roleId; // 对应数据库的外键 role_id
}

