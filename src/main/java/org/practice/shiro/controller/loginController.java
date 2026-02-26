package org.practice.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/auth")
public class loginController {

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {


        //System.out.println(">>>>>>>>>> 接口被触发了！ <<<<<<<<<<");
        Map<String, Object> result = new HashMap<>();

        // 1. 获取 Subject
        Subject subject = SecurityUtils.getSubject();

        // 2. 封装 Token
        UsernamePasswordToken token = new UsernamePasswordToken(
                loginData.get("username"),
                loginData.get("password")
        );

        try {
            // 3. 尝试登录
            subject.login(token);
            result.put("code", 200);
            result.put("msg", "Login Success");
        } catch (UnknownAccountException e) {
            result.put("code", 401);
            result.put("msg", "Account does not exist");
        } catch (IncorrectCredentialsException e) {
            result.put("code", 401);
            result.put("msg", "Wrong password");
        } catch (AuthenticationException e) {
            result.put("code", 500);
            result.put("msg", "System error: " + e.getMessage());
        }

        return result;
    }
}