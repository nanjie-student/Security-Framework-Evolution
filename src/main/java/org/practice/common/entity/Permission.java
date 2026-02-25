package org.practice.common.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("permissions")
public class Permission {
    private Integer id;
    private Integer roleId;
    private String permissionCode;
}
