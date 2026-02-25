package org.practice.session.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import jakarta.servlet.http.HttpSession;
import org.practice.common.entity.User;
import org.practice.common.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/Login")
    public String sessionLogin(String username, String password, HttpSession session){
        // 先检查输入是否为空 (Check if input is empty first)
        if (username == null || password == null) {
            return "Please provide username and password in the URL!";
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));

        if (user == null || !user.getPassword().equals(password)){
            return "username or password is wrong";
        }

        session.setAttribute("currentUser", user);
        return "Success! Welcome " + user.getUsername();

    }
    @GetMapping("/getInfo")
    public String getInfo(HttpSession session){
        Object user = session.getAttribute("currentUser");
        if(user == null){
            return "尚未登陆，清先访问/session/Login";
        }
        return "你已登陆，用户信息为：" + user.toString();

    }
}
