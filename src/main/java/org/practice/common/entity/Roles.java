package org.practice.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("roles")
public class Roles {
    private Integer id;
    private String roleName;
}
