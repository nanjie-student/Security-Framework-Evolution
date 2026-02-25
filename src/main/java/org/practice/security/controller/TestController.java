package org.practice.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // 只要登录就能访问
    @GetMapping("/Hello")
    public String hello(){

        return "Hello,you're logged in";

    }

    // 只有拥有 'user:add' 权限的角色（比如 ADMIN）能访问
    @GetMapping("/Admin/add")
    @PreAuthorize("hasAuthority('user:add')")
    public String add(){
        return "Permission Granted: You can add user";
    }

    // 只有拥有 'user:list' 权限的角色（ADMIN 和 USER 都有）能访问
    @GetMapping("/user/list")
    @PreAuthorize("hasAuthority('user:list')")
    public String list(){
        return "Permission Granted: You can list users";
    }

}
