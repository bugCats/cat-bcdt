# cat-bcdt
Bugcat DevOps Tool —— 面向小微企业研发团队的 DevOps 研发运维管理平台。主要功能有：

1. **网址导航管理**

    维护团队内部常用网址链接，提供统一内部导航页面；

2. **数据库SQL管理、SQL计划任务**

   按日期归档管理SQL脚本，实现数据库变更记录留存、变更追溯；
   
   定时自动执行SQL脚本，适合夜间低峰执行大事务SQL，避开业务高峰；

3. **数据库连接管理**

   统一维护多数据源配置，为SQL执行、数据库字段加解密、代码生成提供数据源；

4. **代码生成**

   基于数据表结构一键生成实体类、Mapper、Dao、Service等业务层代码；生成模板支持自定义修改；

5. **数据库连接授权管理**

    使用代理，只有在bcdt授权通过的用户，才能使用临时账号链接数据库；数据库实际链接信息完全不可见；临时账号可根据需求设置变动频率；

    支持读写账号、只读账号、DBA账号配置，完成读写分离与权限隔离；

    JDBC、Navicat等任意客户端访问，均可记录SQL语句、执行耗时；所有操作持久化可溯源；

6. **线上日志查阅**
 
    可通过web界面，可视化查阅服务器上指定的日志文件；优化根据关键词搜索日志内容；

7. **禅道、Jenkins、Gitea集成**

    对接常用的开发工具、系统，统一接收WebHook事件，集中处理消息；

8. **团队协作**

   个人日报填写，关联禅道任务/Bug工时统计；支持团队日报汇总；

   标准化提测邮件，降低研发与测试沟通成本

<br>

<br>

---

<br>

## 项目截图

截图位于`docs/images/`：

[工作台](docs/images/工作台.png) [开发工具](docs/images/开发工具.png) [网址导航1](docs/images/超链接1.png) [网址导航2](docs/images/超链接2.png) [日志1](docs/images/日志1.png) [日志2](docs/images/日志2.png) [团队协作1](docs/images/团队协作1.png) [团队协作2](docs/images/团队协作2.png)

<br>

<br>

---

<br>


## 项目简介

<br>

**使用前后的分离**

- 后端：SpringBoot4 + Mybatis-Plus；

- 前端：Vue3 + Element-Plus；

- 数据库直接对接**禅道**数据库，复用现有账号、密码与权限，无需独立开发用户登录、权限模块。

<br>

**工程模块结构**

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
│   ├── boot-common/             # springboot扩展、基础工具类、全局常量、SPI扩展点、统一异常处理、JSON字段自动脱敏、线程池
│   ├── boot-webmvc/             # SpringMVC主要功能。Controller/Service切面、接口鉴权、全局异常、API日志，协议驱动主体实现
│   ├── boot-webflux/            # 响应式WebFlux框架。面向IO密集型场景（日志检索服务使用）、独立的websocket服务
│   ├── boot-mybatis/            # MyBatis 扩展。Mapper类二次JDK代理，支持AOP扩展；数据库字段自动加解密
│   ├── boot-mybatis-log/        # MyBatis SQL 打印优化。控制台输出**携带完整参数、可直接执行的SQL**
│   ├── boot-mariadb/            # MariaDB数据源自动配置封装
│   ├── boot-icache/             # 单机缓存管理
│   ├── boot-gener/              # 代码生成工具。根据数据表结构一键生成实体、Mapper、Service代码（使用 FreeMarker 模板）
│   ├── boot-utils-sm/           # 国密 SM 工具
│   ├── boot-utils-freemarker/   # FreeMarker 工具封装
│   └── boot-utils-http/         # HTTP 工具封装
│
└── cat-bcdt-web/                # Vue 3 前端（开发端口 11022）
```

<br>

<br>

## 模块详细说明

### 业务平台 cat-bcdt-ops

主业务系统，启动类：`cc.bugcat.bcdt.ops.BcdtOpsApplication`，默认端口：**11010**

<br>


### 日志服务 cat-bcdt-logsev

独立解耦日志服务，避免大文件读取拖慢主业务平台；启动类：`cc.bugcat.bcdt.logsev.BcdtLogsevApplication`；基于WebFlux，默认端口 **18232**

- GB级日志文件分页读取：采用文件随机行定位算法，无需加载完整日志文件；

- 关键词检索，搜索结果高亮，支持分页上下翻页；

- WebSocket实时tail-f，模拟`tail -f`实时查看日志新增输出；

- 多应用、多实例日志目录配置管理；

- Heapdump文件监听：检测OOM dump文件生成，触发钉钉告警通知。

<br>

### 前端 cat-bcdt-web

构建输出目录：`../webdist/dist`，部署base路径 `/web/`


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

<br>

<br>

---

<br>

## 快速开始


### 环境前置要求

- JDK 25+
- Maven 3.6+
- Node.js `^20.19.0 || >=22.12.0`
- MariaDB / MySQL（复用禅道数据库，若无禅道环境需要手动导入禅道库结构）

<br>

### 数据库

- 如果团队内部署了禅道，bcdt可以直接使用禅道数据库，运行`sql/0-init.sql`，初始化bcdt项目表结构、和基础数据；
- 如果不想使用禅道数据库，需要先执行`sql/0-init-zentao.sql`之后，再执行`sql/0-init.sql`；此种方式默认账号/密码：`admin`/`Bugcat6;`
- 修改`cat-bcdt-ops/src/main/resources/config/application-local.properties`配置文件中数据源链接方式；

<br>

### 后端本地启动

1. 克隆代码，在IDE中选择cat-bcdt/pom.xml，使用Maven完成依赖下载；
1. 修改对应环境配置文件，配置数据库连接、禅道地址等参数；
1. 启动主业务平台 `BcdtOpsApplication`；
1. 按需启动日志服务 `BcdtLogsevApplication`。
1. 项目使用**协议驱动**，主要业务逻辑代码在`openapi`包中。
1. 登录时会检查密码强度，测试时可以去掉`cc.bugcat.bcdt.ops.utils.PasswordCheckUtil#checkPassword`校验，避免登录不了；

<br>

### 前端本地启动

1. 进入`cat-bcdt-web`目录执行`npm install`安装依赖；
1. 项目可更改配置文件：`src/data/global.config.js`；后台接口配置：`src/utils/http/conn.js`；
1. 执行启动命令`bc:dev`，运行开发服务；
1. 开发服务器启动完成后访问：`http://localhost:11022/web/`

> 与后台交互地方`src/utils/http/conn.js` `src/utils/http/ajax.js`，采用`集中式ajax配置管理方案`，[~~*没错，还是我*~~](https://blog.csdn.net/qq_41399429/article/details/79913754)

<br>

### 其他内容

项目中还有一些其他功能未列出，可以自行查看代码，类上均有注释；
如果包中存在`package-info.java`文件，内包含了对整个包的功能描述、和注意事项。


<br>

<br>

---


<br>


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

插件详细介绍[请戳这里](https://www.bugcat.cc/bre/)  ~~*没错，我就是为了这碟醋，包的这盘饺子*~~



### 协议驱动

[协议驱动介绍](docs/协议驱动.md)

### 账号权限

[账号权限](docs/账号权限.md)

### 网址配置

[网址配置](docs/网址配置.md)

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

