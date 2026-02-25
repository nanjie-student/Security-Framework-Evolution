Spring Boot 3 权限框架迁移技术复盘
1. 问题背景
   在将项目升级至 Spring Boot 3 后，系统全面放弃了传统的 javax.* 命名空间，转而采用 Jakarta EE 10 (jakarta.*)。由于权限框架 Apache Shiro 对此标准的适配不够彻底，导致了严重的底层冲突。

2. 核心冲突点
   命名空间断层：Spring Boot 3 的 Filter 接口来自 jakarta.servlet，而引入的 Shiro 2.0 依赖（默认版）内部字节码仍死守 javax.servlet。

类型不匹配：在配置类中尝试注入 Filter 时，编译器报错：Map<String, jakarta.servlet.Filter> 无法转换为 Map<String, javax.servlet.Filter>。即使类名相同，由于包名不同，Java 视其为完全不同的类型。

3. 排查过程与障碍
   依赖地狱 (Dependency Hell)：尝试使用 classifier: jakarta 标签强制下载适配包，但受限于 Maven 的本地缓存机制（.lastUpdated 文件），导致新包无法正确拉取。

命令缺失：由于系统环境变量未配置 mvn 路径，无法通过命令行强制刷新缓存，增加了排查难度。

4. 最终决策
   考虑到 Shiro 在 Jakarta 生态下的不稳定性及维护成本，决定放弃硬攻 Shiro 依赖，转向 Spring Security。后者作为 Spring 官方组件，提供了原生的 Jakarta 支持，彻底消除了包名冲突。

Technical Post-Mortem on Security Framework Migration
1. Context
   Following the upgrade to Spring Boot 3, the project transitioned from the legacy javax.* namespace to Jakarta EE 10 (jakarta.*). This shift triggered a significant underlying conflict with Apache Shiro, which has not fully modernized its internal dependencies.

2. Root Cause Analysis
   Namespace Disconnect: The Filter interface in Spring Boot 3 originates from jakarta.servlet, whereas the default Shiro 2.0 binary remains hard-coded to the legacy javax.servlet API.

Type Incompatibility: When defining the configuration bean, the compiler flagged a type mismatch: Map<..., jakarta.servlet.Filter> cannot be converted to Map<..., javax.servlet.Filter>. In Java, identical class names in different packages are treated as incompatible types.

3. Challenges & Roadblocks
   Dependency Hell: Attempts to resolve the issue using the jakarta classifier were hindered by Maven's local cache. The presence of .lastUpdated files prevented the IDE from fetching the correct artifacts.

Tooling Constraints: The absence of the mvn command in the system's environment variables made it difficult to force-update the repository via CLI, complicating the troubleshooting process.

4. Final Decision
   Due to the friction in Shiro’s Jakarta implementation, we decided to pivot to Spring Security. As a first-party component, Spring Security offers native Jakarta support, effectively eliminating namespace conflicts and reducing long-term maintenance overhead.