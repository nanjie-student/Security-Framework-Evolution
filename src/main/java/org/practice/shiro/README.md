Post-Mortem: Shiro Migration to Spring Boot 3 & Jakarta EE

        1. Problem Statement
        The upgrade to Spring Boot 3 introduced a breaking change: the mandatory transition from the legacy javax.* 
        namespace to Jakarta EE 10 (jakarta.*). Standard Apache Shiro starters lacked full compatibility, leading to
        Binary Incompatibility where Shiro’s filters expected javax.servlet while the Spring container provided jakarta.servlet.

        2. Core Conflict: Namespace Mismatch
        The primary symptom was a Type Mismatch in the configuration layer. Even though the class names were identical
        (e.g., Filter), the different package origins caused a ClassCastException or compilation errors when attempting 
        to inject custom filters into the ShiroFilterFactoryBean.

        3. Root Cause Analysis (RCA) & Troubleshooting
        Dependency Hell: Using the Shiro Starter acted as a "Black Box," hiding the transitive javax dependencies.
        Maven Cache Corruption: Local repository artifacts (specifically .lastUpdated files) initially prevented the 
        successful download of the Jakarta-classified Shiro binaries.
        Toolchain Configuration: Encountered environment-level friction, including Lombok annotation processing hurdles 
        and TCP port conflicts (Port 8080), which emphasized the importance of a clean development environment.

        4. Technical Solution: The "Manual-First" Strategy
        To gain full architectural control, I pivoted from Auto-configuration to Explicit Wiring:
        Granular Dependency Management: Excluded the shiro-spring-boot-web-starter. Instead, I manually 
        declared shiro-core, shiro-web, and shiro-spring using the <classifier>jakarta</classifier> tag.    
        Explicit Bean Orchestration: Reconstructed the ShiroConfig class from scratch. By manually defining the 
        SecurityManager and ShiroFilterFactoryBean, I ensured all components were strictly aligned with the 
        jakarta.servlet namespace.
        Persistence Layer Optimization: Resolved BindingException by adopting MyBatis-Plus Lambda QueryWrappers. 
        This eliminated the need for fragile XML-based SQL mappings, moving the project towards a Type-Safe, Code-First
        architecture.

        5. Key Takeaways
        Starter Independence: When auto-configuration fails, the ability to "deconstruct" a framework and wire 
        it manually is a critical senior-level skill.

        Defensive Testing: Utilizing Negative Testing (testing invalid usernames) to isolate bugs between the Security 
        and Persistence layers.

        Future-Proofing: Successfully aligned the project with the Jakarta EE ecosystem, setting a solid foundation for 
        Level 3: Spring Security.



    Auth-Framework: Spring Boot 3 权限架构迁移实战记录
        项目背景
        本项目旨在探索从 原生 Session 到 Apache Shiro，再到 Spring Security 的权限架构演进。在 Level 2 (Shiro 实战) 阶段，由于项目环境升级至 
        Spring Boot 3 (Jakarta EE 10)，遭遇了严重的生态兼容性挑战。
        技术复盘：从“依赖地狱”到“手动挡”突围

        1. 核心冲突：命名空间的“代沟”
            现象：在注入过滤器时遭遇 Incompatible types 报错。
            本质：Spring Boot 3 强制要求 jakarta.servlet，而 Shiro 默认版本仍依赖 javax.servlet。这种“包名不同即为异类”的 Java 特性
            导致了代码层面的断层。

        2. 依赖管理：破除 Maven 缓存魔咒
            坑点：配置了 classifier: jakarta 但 IDE 依然报错。
            解决：清理 .lastUpdated 损坏文件，手动配置 dependencyManagement。通过“去 Starter 化”策略，放弃黑盒自动配置，手动引入
            模块化构件。

        3. 手动挡配置：夺回控制权
            方案：删除了无法识别的 autoconfigure 包导入。
            实践：手动声明 SecurityManager 与 ShiroFilterFactoryBean。这一步让我彻底理解了 Shiro 是如何通过 LinkedHashMap 编排
            过滤器链（Filter Chain）的底层原理。

        4. 环境治理：端口与工具链优化
            问题：遭遇 Port 8080 already in use 及 Lombok annotation processing 报错。
            心得：意识到高效开发不仅是写代码，更需要对 IDE 工具链（如编译器注解处理器）和 操作系统资源（进程端口管理）
            有精准的掌控。

        5. 持久层进阶：MyBatis-Plus 的“去 XML”哲学
            优化：遇到 BindingException 映射异常。
            突破：拒绝回归传统的 XML 编写，直接利用 MP 的 LambdaQueryWrapper 实现了类型安全的动态查询。

反思：
