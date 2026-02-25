package org.practice.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 开启我们刚才说的 @PreAuthorize 权限注解
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 练习时通常关闭 CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll() // 公开接口，随便看
                        .anyRequest().authenticated()             // 其他所有接口，必须登录
                )
                .formLogin(form -> form.permitAll());         // 使用框架自带的登录表单

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 告诉系统：暂时不要加密，用明文比对（练习用）
        return NoOpPasswordEncoder.getInstance();
    }
}
