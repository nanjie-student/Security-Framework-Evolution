package org.practice;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        // 排除掉这个会自动去找 javax.servlet 的类
        org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration.class
})
@MapperScan("org.practice.common.mapper") // 扫描刚才写的 Mapper
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
