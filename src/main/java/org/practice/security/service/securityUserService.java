package org.practice.security.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.practice.common.entity.Permission;
import org.practice.common.entity.User;
import org.practice.common.mapper.PermissionMapper;
import org.practice.common.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class securityUserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //分步查询，先查询用户，再查询权限
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username",username));

        //打印输出，简单测试
        // 加这两行打印
        //System.out.println("--- 正在登录的用户: " + username);
        //System.out.println("--- 数据库查到的密码: " + (user != null ? user.getPassword() : "未找到用户"));

        if (user == null){
            throw new UsernameNotFoundException("User not found");
        }
        List<Permission> permissions = permissionMapper.selectList(new QueryWrapper<Permission>().eq("role_id",user.getRoleId()));


        /// 3. 将权限代码转换为 Security 的 GrantedAuthority 对象
        // 这里的 permission_code 对应数据库里的 'user:add', 'user:list' 等
        List<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermissionCode()))
                .collect(Collectors.toList());

        // 4. 包装成 Security 认识的 User 对象返回
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // 确保数据库是明文或已配置对应的 Encoder
                authorities
        );
    }
}
