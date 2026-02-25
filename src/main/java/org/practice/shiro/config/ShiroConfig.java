package org.practice.shiro.config;



import jakarta.servlet.Filter;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfig {


    @Bean
    public MyRealm MyRealm(){
        return new MyRealm();
    }


    // 2. 配置 SecurityManager (报错的关键就在这里)
    @Bean
    public DefaultWebSecurityManager securityManager(MyRealm myRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 如果漏掉下面这一行，MyRealm 里的 System.out 就永远不会执行！
        securityManager.setRealm(myRealm);
        return securityManager;
    }

    // 3. 配置 ShiroFilter (必须要这个，否则 SecurityManager 不会绑定到请求线程)
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        // 核心修正：
        // 手动定义一个 Filter 映射表。如果不写，Shiro 默认会去初始化
        // 那些包含 javax 代码的默认过滤器（如 FormAuthenticationFilter）。
        Map<String, Filter> filters = new LinkedHashMap<>();
        factoryBean.setFilters(filters);

        // 设置路径拦截规则
        Map<String, String> chainDefinition = new LinkedHashMap<>();
        chainDefinition.put("/auth/login", "anon");
        chainDefinition.put("/**", "authc");
        factoryBean.setFilterChainDefinitionMap(chainDefinition);

        return factoryBean;
    }
}
