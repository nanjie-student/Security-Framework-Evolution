package org.practice.shiro.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.practice.common.entity.User;
import org.practice.common.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;
    /**
     * 授权 查权限时调用
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        // 逻辑：根据用户名去数据库查权限，塞进 SimpleAuthorizationInfo
        return new SimpleAuthorizationInfo();
    }

    /**
     * 认证：登陆时调用
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //输入用户名密码
        String username = (String)token.getPrincipal();
        System.out.println("输入的用户名是："+ username);
        // 从数据库中查找对应的密码
        User user = userMapper.findByUsername(username);
        if(user == null){
            return null;
        }


        // 返回认证信息：参数(用户名, 数据库密码, Realm名称)
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
