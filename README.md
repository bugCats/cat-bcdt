# cat-bcdt
> Bugcat DevOps Tool —— 面向小微企业研发团队的一站式 DevOps 研发运维管理平台

## 目录
- [项目简介](#项目简介)
- [核心价值](#核心价值)
- [核心能力](#核心能力)
- [技术栈](#技术栈)
  - [后端技术栈](#后端技术栈)
  - [前端技术栈](#前端技术栈)
- [工程模块结构](#工程模块结构)
- [模块详细说明](#模块详细说明)
  - [核心框架 cat-bcdt-core](#核心框架-cat-bcdt-core)
  - [业务平台 cat-bcdt-ops](#业务平台-cat-bcdt-ops)
  - [日志服务 cat-bcdt-logsev](#日志服务-cat-bcdt-logsev)
  - [前端 cat-bcdt-web](#前端-cat-bcdt-web)
- [快速开始](#快速开始)
  - [环境前置要求](#环境前置要求)
  - [后端本地启动](#后端本地启动)
  - [前端本地启动](#前端本地启动)
- [配置参考](#配置参考)
  - [业务平台 cat-bcdt-ops 配置](#业务平台-cat-bcdt-ops-配置)
  - [日志服务 cat-bcdt-logsev 配置](#日志服务-cat-bcdt-logsev-配置)
- [构建与生产部署](#构建与生产部署)
  - [后端打包构建](#后端打包构建)
  - [前端打包构建](#前端打包构建)
- [API 接口文档](#api-接口文档)
- [更多文档](#更多文档)
  - [协议驱动](#协议驱动)
  - [账号权限](#账号权限)
  - [网址配置](#网址配置)
  - [Jenkins集成](#jenkins集成)
  - [服务器部署](#服务器部署)
- [许可证](#许可证)
---


## 项目简介

**cat-bcdt（Bugcat DevOps Tool）** 是专为小微企业研发团队打造的一站式私有化DevOps运维管理平台。针对中小团队预算有限、人员少、不想维护多套重型中间件的现状，聚焦数据库运维管控、CI/CD流水线对接、线上日志观测、团队研发协作等日常高频场景。同时，项目内置的`cat-bcdt-core`核心框架，可独立剥离使用，帮助开发者快速搭建新项目，大幅提升项目初始化与开发效率。

✅ **架构特性：前后端分离**

- 后端：Spring Boot 4 多模块 Maven 工程，内置自研**协议驱动开发框架**，封装大量通用能力，简化业务开发；

- 前端：Vue3 + Element Plus 单页应用；

- **无缝复用禅道账号体系**：直接对接禅道数据库，复用现有账号、密码与权限，无需独立开发用户登录、权限模块，大幅降低接入成本。


> 项目定位：轻量化私有化部署工具，贴合国内中小团队工作流程，解决数据库操作风险、日志排查繁琐、研发协同碎片化等实际痛点。


## 核心价值

1. 🛡️ **数据库安全管控**：SQL变更版本留痕、权限申请审批、SQL审计、临时账号安全隔离，减少人为误操作带来的数据风险；

2. ⚡ **降低团队运维负担**：把日志检索、数据库代理、CI联动、团队协作能力整合在一套系统，无需部署多款第三方重型工具；

3. 🤝 **打通现有研发工具链**：对接禅道、Gitea、GitLab、Jenkins，复用已有系统，避免业务割裂；

4. 🚀 **提升开发效率**：内置数据表代码生成器、数据库字段自动加解密、标准化提测模板，减少重复手工工作；

5. 📈 **流程可追溯**：数据库操作、审批记录、SQL审计日志全部持久化留存，便于事后排查复盘。


## 核心能力

1. 🗄️ **数据库运维管理**：SQL脚本版本管控、定时SQL计划任务、数据库账号权限申请审批流程；

2. 🔐 **MySQL TCP代理服务**：MySQL5.7 协议层代理转发、全量SQL审计、读写分离、临时账号安全隔离；

3. 🧩 **代码自动生成**：基于数据表结构一键生成实体、Mapper、Service等业务层代码，模板支持自定义；

4. 📜 **独立日志观测服务**：支持GB级日志在线检索、实时模拟`tail-f`、OOM Heapdump异常钉钉告警；

5. 🔗 **第三方系统联动**：对接Gitea / GitLab / Jenkins / 禅道 WebHook，实现事件联动、消息推送；

6. 👥 **团队研发协作**：个人&团队日报工时统计、任务看板、消息待办通知、标准化提测邮件模板。

---

## 技术栈

### 后端技术栈
| 分类 | 组件 | 版本 |
|------|------|------|
| 运行环境 | Java | 25 |
| 应用框架 | Spring Boot | 4.0.5 |
| ORM框架 | MyBatis-Plus（spring-boot4-starter） | 3.5.16 |
| 数据库驱动 | MariaDB Java Client | 3.5.8 |
| 连接池 | HikariCP | 7.0.2 |
| 网络代理 | Vert.x Core | 4.5.29 |
| API文档 | knife4j OpenAPI3 Jakarta | 4.5.0 |
| API文档 | springdoc-openapi | 3.1.0 |
| 模板引擎 | FreeMarker | - |
| 内置组件 | Spring WebSocket / Mail / AOP / Validation | - |



### 前端技术栈
| 分类 | 技术 | 版本 |
|------|------|------|
| 核心框架 | Vue | 3.5.x |
| 路由 | Vue Router | 4.5.x |
| 状态管理 | Pinia | 3.0.x |
| UI组件库 | Element Plus | 2.10.x |
| 构建工具 | Vite | 7.0.x |
| Node版本 | Node.js | ^20.19.0 \|\| >=22.12.0 |


---


## 工程模块结构

多模块Maven项目，整体分为核心框架、业务平台、独立日志服务、前端四大部分。


```text
cat-bcdt-manage/
├── cat-bcdt/                    # 聚合构建模块（cc.bugcat:cat-bcdt）
│   ├── cat-bcdt-ops/            # 业务核心：运维管理平台（端口 11010）
│   ├── cat-bcdt-logsev/         # 独立日志检索服务（端口 18232）
│   └── docs/                    # 项目文档与截图
│
├── cat-bcdt-core/               # 自研快速开发框架（cc.bugcat.bcdt:cat-bcdt-core）
│   ├── boot-parent/             # 父 POM，统一依赖版本
│   ├── boot-common/             # 核心公共类、工具类、SPI 扩展点
│   ├── boot-webmvc/             # 协议驱动 WebMVC 框架
│   ├── boot-webflux/            # 响应式 WebFlux 框架
│   ├── boot-mybatis/            # MyBatis 扩展（JDK 二次代理、字段加解密）
│   ├── boot-mybatis-log/        # MyBatis SQL 打印优化
│   ├── boot-mariadb/            # MariaDB 驱动封装
│   ├── boot-icache/             # 单机缓存管理
│   ├── boot-gener/              # 代码生成工具（FreeMarker 模板）
│   ├── boot-utils-sm/           # 国密 SM 工具
│   ├── boot-utils-freemarker/   # FreeMarker 工具封装
│   └── boot-utils-http/         # HTTP 工具封装
│
└── cat-bcdt-web/                # Vue 3 前端（开发端口 11022）
```

## 模块详细说明

### 核心框架 cat-bcdt-core

**协议驱动开发框架**，通过`boot-parent`统一管控全部依赖版本，业务模块按需引入子组件。

| 模块 | 能力简述                                                      |
|-------------|-----------------------------------------------------------|
| boot-common | springboot扩展、基础工具类、全局常量、SPI扩展点、统一异常处理、JSON字段自动脱敏、线程池      |
| boot-webmvc | Controller/Service切面、接口鉴权、全局异常、API日志，协议驱动主体实现             |
| boot-webflux | WebFlux封装，面向IO密集型场景（日志检索服务使用）、独立的websocket服务              |
| boot-mybatis | Mapper类二次JDK代理，支持AOP扩展；数据库字段自动加解密 |
| boot-mybatis-log | 控制台输出**携带完整参数、可直接执行的SQL**，提升调试效率                          |
| boot-icache | 单机内存缓存                                                    |
| boot-gener | 根据数据表结构一键生成实体、Mapper、Service代码；代码模板支持自定义修改                |
| boot-mariadb | MariaDB数据源自动配置封装                                          |
| boot-utils-* | 国密SM、FreeMarker、HTTP请求等通用工具包                              |



### 业务平台 cat-bcdt-ops

> 主业务系统，启动类：`cc.bugcat.bcdt.ops.BcdtOpsApplication`，默认端口：**11010**



#### ✨ 主要业务功能

1. **网址导航管理**：维护团队内部常用系统链接，提供统一内部导航页面；

2. **数据库连接管理**：统一维护多数据源配置，为SQL执行、字段加解密、代码生成提供数据源支撑；

3. **SQL版本管理**：按日期归档管理SQL脚本，实现数据库变更记录留存、变更追溯；

4. **数据库计划任务**：定时自动执行SQL脚本，适合夜间低峰执行大事务SQL，避开业务高峰；

5. **数据库字段加解密**：配合注解`@Encrys`，提供可视化操作页面，支持加密字段查看与修改；

6. **MySQL5.7 TCP数据库代理（核心模块）** *（目前只支持 MySQL5.7 及以下版本代理）*
   - 基于Vert.x实现TCP层MySQL协议代理，双向转发数据包；
   - 支持读写账号、只读账号、DBA账号配置，完成读写分离与权限隔离；
   - **申请-审批流程**：开发提交访问申请，审批通过自动生成临时MySQL子账号；
   - **SQL全量审计**：JDBC、Navicat等任意客户端访问，均解析协议记录SQL语句、执行耗时；空闲1小时自动断开连接；所有操作持久化可溯源；
   - 安全防护：数据库真实IP、账号密码开发者完全不可见；临时账号可根据需求设置变动频率。



7. **数据库权限申请审批**

   | 环节 | 接口地址 | 说明 |
   |---|---|---|
   | 提交申请 | `/auth/code/apply` | 用户提交数据库访问授权码申请 |
   | 管理员审批 | - | 后台审核操作 |
   | 审批通过下发 | `/auth/code/adopt` | 审批通过生成有效授权码 |
   | 授权码校验 | `/auth/code/valid` | 校验授权码有效性 |
   | 查询个人账号信息 | `/auth/code/info` | 查询自己授权账号详情 |

8. **CI/CD系统集成**
  - **Jenkins**：批量维护项目分支；接收WebHook构建事件，推送钉钉通知；
  - **Gitea / GitLab**：接收push、MR事件，触发Jenkins构建；检测高危文件提交；支持模拟SSO单点登录；
  - **禅道对接**：复用禅道账号库；查询任务列表；接收禅道WebHook同步推送任务状态变更。

9. **团队协作能力**
  - 日报系统：个人日报填写，关联禅道任务/Bug工时统计；支持团队日报汇总、管理看板；
  - 消息&待办：统一消息通知中心；
  - 提测邮件模板：标准化提测邮件，降低研发与测试沟通成本。


### 日志服务 cat-bcdt-logsev

> 独立解耦日志服务，避免大文件读取拖慢主业务平台；启动类：`cc.bugcat.bcdt.logsev.BcdtLogsevApplication`；基于WebFlux，默认端口 **18232**


**核心能力：**

- GB级日志文件分页读取：采用文件随机行定位算法，无需加载完整日志文件；

- 关键词检索，搜索结果高亮，支持分页上下翻页；

- WebSocket实时tail-f，模拟`tail -f`实时查看日志新增输出；

- 多应用、多实例日志目录配置管理；

- Heapdump文件监听：检测OOM dump文件生成，触发钉钉告警通知。



### 前端 cat-bcdt-web

Vue3 + Vite7 SPA单页应用
- 构建输出目录：`../webdist/dist`，部署base路径 `/web/`


**主要页面目录映射**

| views目录 | 模块功能 |
|---|---|
| links | 网址导航管理 |
| dbset | 数据库连接配置 |
| dbsql | SQL版本管理 |
| dbtask | SQL定时计划任务 |
| dbcoder | 数据库字段加解密工具 |
| dbproxy | MySQL数据库代理管理 |
| dbauth | 数据库权限申请审批 |
| jenkins | Jenkins集成配置 |
| note | 日报、团队统计看板 |
| chat | 消息待办通知 |
| email | 提测邮件模板 |
| logger | 日志检索查看 |
| user | 用户中心 |
| login | 登录页面 |


---


## 快速开始


### 环境前置要求

- JDK 25+
- Maven 3.6+
- Node.js `^20.19.0 || >=22.12.0`
- MariaDB / MySQL（复用禅道数据库，若无禅道环境需要手动导入禅道库结构）


### 后端本地启动

> 1. 克隆代码，在IDE中选择cat-bcdt/pom.xml，使用Maven完成依赖下载；
> 2. 修改对应环境配置文件，配置数据库连接、禅道地址等参数；
> 3. 启动主业务平台 `BcdtOpsApplication`；
> 4. 按需独立启动日志服务 `BcdtLogsevApplication`。


### 前端本地启动

> 1. 进入`cat-bcdt-web`目录执行`npm install`安装依赖；
> 2. 执行启动命令运行开发服务；
> 3. 开发服务器启动完成后访问：`http://localhost:11022/web/`


---


## 配置参考

项目采用 **Properties** 格式配置文件，支持多环境切换（`local` / `prod`）。

### 业务平台 cat-bcdt-ops 配置

主配置文件：`src/main/resources/application.properties`

| 配置项 | 默认值 | 说明 |
|--------|------| ---- |
| `spring.profiles.active` | local | 激活的环境配置 |
| `cross.domain.enabled` | true | 是否允许跨域 |


环境配置文件：`src/main/resources/config/application-{env}.properties`

| 配置项 | 说明 |
|--------|------|
| `spring.datasource.url` | 数据源地址（默认连接禅道库 `zentao`） |
| `spring.datasource.username` | 数据库用户名 |
| `spring.datasource.password` | 数据库密码 |
| `spring.datasource.hikari.*` | HikariCP 连接池参数 |
| `zentao.web-host` | 禅道网页访问地址 |
| `bcdt.web-host` | bcdt 后台服务访问地址 |
| `mybatis.sql.show` | 是否打印完整SQL |
| `scheduled.enabled` | 是否开启定时任务 |


### 日志服务 cat-bcdt-logsev 配置

主配置文件：`src/main/resources/application.properties`

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| `server.port` | 18232 | 服务端口 |
| `inner-cache.serializable` | false | 缓存序列化开关 |
| `cross.domain.enabled` | true | 是否允许跨域 |

环境配置文件：`src/main/resources/config/application-{env}.properties`

| 配置项 | 说明 |
|--------|------|
| `app.bcdt-path` | 办公平台地址（用于服务回调） |
| `logger.config` | 日志文件路径配置（JSON数组，指定应用名、目录、标签） |
| `heap-dump.dir-path` | Heapdump 文件监听目录 |
| `heap-dump.listen-enable` | 是否开启 heapdump 文件监听 |
| `heap-dump.ding-token` | OOM内存溢出告警钉钉群机器人Token |


---


## 构建与生产部署


### 后端打包构建

- **分离打包策略**：通过`maven-dependency-plugin`把第三方依赖输出到`target/lib`，自研模块输出到`target/applib`；业务jar不内嵌依赖，方便生产环境增量更新；

- **最终产物**：业务jar包 + `lib/`目录 + `applib/`目录；使用`java -jar`配合`-Dloader.path`参数完成启动。

> 启动命令：nohup java -jar -Dloader.path=lib,applib cat-bcdt-ops*.jar


### 前端打包构建

构建产物输出目录：`../webdist/dist`，可交由Nginx托管，也可集成到后端静态资源服务。

----

## API 接口文档

项目集成 knife4j + springdoc-openapi，服务启动后访问地址：
- **Knife4j UI**：`http://localhost:11010/doc.html`
- **OpenAPI JSON**：`http://localhost:11010/v3/api-docs`

----

## 更多文档

推荐一款浏览器插件，面向后端开发者高效调试API接口的浏览器插件，专为本地联调、接口替换、线上问题复现场景设计，大幅提升开发与调试效率！

插件详细介绍[请戳这里](https://www.bugcat.cc/bre/)  ~~*没错，我就是为了这盘醋，包的这盘饺子*~~



### 协议驱动

编写中...

### 账号权限

编写中...

### 网址配置

编写中...

### Jenkins集成

编写中...

### 服务器部署

编写中...

----

## 许可证

### **cat-bcdt-core** 遵循 `Apache 2.0` 协议；

### **cat-bcdt**、**cat-bcdt-web** 详情见项目中`LICENSE`文件：

1. 项目完全免费，支持个人、企业免费商用，无使用费用；
2. 允许任意二次开发、修改、部署、自用；
3. 禁止行为：禁止将本项目原版、仅轻微修改的功能，单独作为商品售卖、收费授权、出租牟利；
4. 完全放开：经过深度二次开发、重构后的衍生项目，可随意闭源、售卖、商用，无任何限制；
5. 使用时仅需保留原版权声明即可。

