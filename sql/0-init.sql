
-- 注意，项目依赖禅道数据库。如果没有禅道，先先执行 0-init-zentao.sql 初始化必要的表！
-- 注意，项目依赖禅道数据库。如果没有禅道，先先执行 0-init-zentao.sql 初始化必要的表！
-- 注意，项目依赖禅道数据库。如果没有禅道，先先执行 0-init-zentao.sql 初始化必要的表！

-- 项目初始化SQL




USE `zentao`;

CREATE TABLE `bc_db_auth` (
                              `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '数据id',
                              `account` varchar(30) NOT NULL COMMENT '账号',
                              `realname` varchar(100) NOT NULL COMMENT '姓名',
                              `ipAddr` varchar(40) DEFAULT NULL COMMENT '申请时ip',
                              `reason` varchar(255) NOT NULL COMMENT '申请原因',
                              `path` varchar(255) DEFAULT NULL COMMENT '日志目录',
                              `createDate` datetime NOT NULL COMMENT '创建时间',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='数据库申请授权记录表';



CREATE TABLE `bc_db_proxy` (
                               `id` bigint(8) NOT NULL AUTO_INCREMENT COMMENT '数据id',
                               `name` varchar(30) NOT NULL COMMENT '代理名称',
                               `desc` varchar(120) DEFAULT NULL COMMENT '描述',
                               `hostAddr` varchar(128) NOT NULL COMMENT '实际数据库IP+端口',
                               `localPort` int(6) NOT NULL COMMENT '绑定到本地端口',
                               `dbaUser` varchar(64) NOT NULL COMMENT 'dba账号',
                               `dbaPwd` varchar(64) NOT NULL COMMENT 'dba密码',
                               `writeUser` varchar(64) DEFAULT NULL COMMENT '写账号',
                               `writePwd` varchar(64) DEFAULT NULL COMMENT '写密码',
                               `readUser` varchar(64) DEFAULT NULL COMMENT '只读账号',
                               `readPwd` varchar(64) DEFAULT NULL COMMENT '只读密码',
                               `whiteUser` varchar(64) DEFAULT NULL COMMENT '白名单账号',
                               `enable` char(1) NOT NULL DEFAULT '0' COMMENT '是否启用：0禁用；1启用；',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `idx_port` (`localPort`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='数据库代理配置';




CREATE TABLE `bc_db_set` (
                             `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '数据id',
                             `code` varchar(30) NOT NULL COMMENT '数据库名',
                             `desc` varchar(64) NOT NULL COMMENT '数据库描述',
                             `jdbcUrl` varchar(64) NOT NULL COMMENT 'jdbc链接：jdbc:mariadb:',
                             `username` varchar(128) NOT NULL COMMENT '数据库账号',
                             `password` varchar(128) NOT NULL COMMENT '数据库密码',
                             `driverName` varchar(128) NOT NULL COMMENT '驱动',
                             `cryptoType` varchar(20) NOT NULL COMMENT '数据库字段加密方式：sm4',
                             `cryptoKey` varchar(64) DEFAULT NULL COMMENT '数据库字段加密key',
                             `active` char(1) NOT NULL DEFAULT '1' COMMENT '是否有效：1有效；0无效；',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='数据库配置';



CREATE TABLE `bc_link` (
                           `id` varchar(20) NOT NULL COMMENT '数据id',
                           `type` varchar(20) NOT NULL COMMENT '数据类型：text；link',
                           `label` varchar(60) NOT NULL COMMENT '显示内容',
                           `title` varchar(200) DEFAULT NULL COMMENT '链接title',
                           `linkUrl` varchar(2000) DEFAULT NULL COMMENT '超链接url',
                           `winTitle` varchar(200) DEFAULT NULL COMMENT '新开窗口标题',
                           `className` varchar(100) DEFAULT NULL COMMENT '样式名',
                           `acs` varchar(30) DEFAULT NULL COMMENT '权限',
                           `desc` varchar(200) DEFAULT NULL COMMENT '描述',
                           `extra` text DEFAULT NULL COMMENT 'html扩展',
                           `pageId` varchar(30) NOT NULL COMMENT '所属页面',
                           `rowId` varchar(30) DEFAULT NULL COMMENT '所属行',
                           `active` char(1) NOT NULL DEFAULT '1' COMMENT '是否有效',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='超链接按钮';




CREATE TABLE `bc_note` (
                           `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '数据id',
                           `account` varchar(30) NOT NULL COMMENT '账号',
                           `realname` varchar(100) NOT NULL COMMENT '姓名',
                           `submitDate` char(10) NOT NULL COMMENT '提交时间yyyy-mm-dd',
                           `context` text NOT NULL COMMENT '内容',
                           `projectId` bigint(11) NOT NULL COMMENT '项目、任务id',
                           `projectType` int(2) NOT NULL COMMENT '10 zt_task；20 zt_bug',
                           `projectName` varchar(255) NOT NULL COMMENT '项目名称',
                           `costTime` decimal(5,1) NOT NULL DEFAULT 0.0 COMMENT '耗时',
                           `costOrg` varchar(64) NOT NULL DEFAULT '---' COMMENT '开发成本归属组织',
                           `costStatus` char(1) NOT NULL DEFAULT '1' COMMENT '成本是否需要统计：1需要；0不需要；',
                           `saveDate` datetime NOT NULL COMMENT '最后修改时间',
                           PRIMARY KEY (`id`),
                           KEY `idx_account` (`account`),
                           KEY `idx_submitDate` (`submitDate`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='个人日报';




CREATE TABLE `bc_param` (
                            `id` varchar(64) NOT NULL COMMENT '参数名',
                            `value` varchar(255) NOT NULL COMMENT '参数值',
                            `desc` varchar(128) NOT NULL COMMENT '参数描述',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参数表';




CREATE TABLE `bc_sql` (
                          `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '数据id',
                          `account` varchar(30) NOT NULL COMMENT '创建人账号',
                          `realname` varchar(100) NOT NULL COMMENT '创建人姓名',
                          `createDate` datetime NOT NULL COMMENT '创建时间',
                          `dbId` bigint(11) NOT NULL COMMENT '数据库',
                          `sql` varchar(2000) NOT NULL COMMENT '执行的SQL',
                          `desc` varchar(255) DEFAULT NULL COMMENT '描述',
                          `status` char(2) NOT NULL DEFAULT '10' COMMENT '执行状态：10 初始；90 已同步；',
                          `submitAccount` varchar(30) DEFAULT NULL COMMENT '同步人账号',
                          `submitRealname` varchar(100) DEFAULT NULL COMMENT '同步人姓名',
                          `submitDate` datetime DEFAULT NULL COMMENT '同步时间',
                          PRIMARY KEY (`id`),
                          KEY `idx_account` (`account`),
                          KEY `idx_status` (`createDate`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='数据库版本管理';



CREATE TABLE `bc_sql_task` (
                               `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '数据id',
                               `account` varchar(30) NOT NULL COMMENT '创建人账号',
                               `realname` varchar(100) NOT NULL COMMENT '创建人姓名',
                               `createDate` datetime NOT NULL COMMENT '创建时间',
                               `dbId` bigint(11) NOT NULL COMMENT '数据库名',
                               `sql` varchar(2000) NOT NULL COMMENT '待执行的SQL',
                               `desc` varchar(255) DEFAULT NULL COMMENT '描述',
                               `status` char(2) NOT NULL DEFAULT '10' COMMENT '状态：10初始；50已执行；90 执行失败',
                               `taskDate` datetime NOT NULL COMMENT '计划执行时间',
                               `result` varchar(1000) DEFAULT NULL COMMENT '执行结果描述',
                               `executeDate` datetime DEFAULT NULL COMMENT '实际执行时间',
                               PRIMARY KEY (`id`),
                               KEY `idx_taskDate` (`taskDate`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='SQL计划任务';




CREATE TABLE `bc_talk` (
                           `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '数据id',
                           `account` varchar(30) NOT NULL COMMENT '消息归属人账号',
                           `realname` varchar(100) NOT NULL COMMENT '消息归属人姓名',
                           `sendAccount` varchar(30) NOT NULL COMMENT '发送人账号',
                           `sendRealname` varchar(100) NOT NULL COMMENT '发送人姓名',
                           `content` text NOT NULL COMMENT '消息内容',
                           `msgType` int(8) NOT NULL COMMENT '消息类型',
                           `msgTags` varchar(40) NOT NULL COMMENT '消息标记',
                           `read` char(1) NOT NULL DEFAULT '0' COMMENT '是否已读：0未读；1已读',
                           `saveDate` datetime NOT NULL COMMENT '最后修改时间',
                           PRIMARY KEY (`id`),
                           KEY `idx_date` (`saveDate`,`read`),
                           KEY `idx_user` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息';



-- 初始化参数表
insert into `bc_param` (`id`, `value`, `desc`) values('ding:admin','','管理人员群token');
insert into `bc_param` (`id`, `value`, `desc`) values('ding:agent_id','','钉钉消息推送到个人：agent_id');
insert into `bc_param` (`id`, `value`, `desc`) values('ding:appkey','','钉钉消息推送到个人：appkey');
insert into `bc_param` (`id`, `value`, `desc`) values('ding:appsecret',' ','钉钉消息推送到个人：appsecret');
insert into `bc_param` (`id`, `value`, `desc`) values('ding:db-auth:read','','数据库授权，只读账号授权码，群token');
insert into `bc_param` (`id`, `value`, `desc`) values('ding:db-auth:write','','数据库授权，读写账号授权码，群token');
insert into `bc_param` (`id`, `value`, `desc`) values('ding:developer','','开发人员群token');



-- 初始化超链接表，根据实际情况新增、编辑
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11201000','label','公共系统',NULL,NULL,NULL,'link-two',NULL,NULL,NULL,'desktop',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11201010','link','{{禅道}} [http://zentao.my/]',NULL,'http://zentao.my/',NULL,NULL,NULL,NULL,NULL,'desktop',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11201020','link','GIT 管理平台',NULL,'http://gitea.my/',NULL,NULL,NULL,NULL,NULL,'desktop','11201020','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11201030','link','代码检查',NULL,'http://sonarqube.my/app/index.html',NULL,NULL,NULL,NULL,NULL,'desktop','11201020','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11201040','link','Jenkins',NULL,'http://jenkins.my/',NULL,NULL,NULL,NULL,NULL,'desktop','11201040','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11201050','link','分支管理',NULL,'#/home/jksbr','分支管理',NULL,NULL,NULL,NULL,'desktop','11201040','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11201060','link','JumpServer 堡垒机',NULL,'https://jump.my/',NULL,NULL,NULL,NULL,NULL,'desktop','11201060','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11201070','link','IP 花名册',NULL,'#/home/userip','用户名册',NULL,NULL,NULL,NULL,'desktop','11201060','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11201090','link','maven 私服',NULL,'http://maven.my/nexus',NULL,NULL,NULL,NULL,NULL,'desktop','11201090','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11201100','link','[setting.xml]',NULL,'./html/maven-setting.html',NULL,NULL,NULL,NULL,NULL,'desktop','11201090','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11201140','link','OA接口调试',NULL,'$/oa/testing','OA调试',NULL,NULL,NULL,NULL,'desktop','11201130','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11301000','label','常用网址',NULL,NULL,NULL,'link-two',NULL,NULL,NULL,'desktop',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11301010','link','百度一下',NULL,'https://www.baidu.com/',NULL,NULL,NULL,NULL,NULL,'desktop','11301010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11301011','link','必应',NULL,'https://cn.bing.com/',NULL,NULL,NULL,NULL,NULL,'desktop','11301010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11301021','link','Json 格式化',NULL,'http://www.bejson.com/jsonviewernew/',NULL,NULL,NULL,NULL,NULL,'desktop','','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11301030','link','翻译',NULL,'https://fanyi.baidu.com/',NULL,NULL,NULL,NULL,NULL,'desktop','11301030','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11301040','link','在线流程图',NULL,'https://www.iodraw.com/',NULL,NULL,NULL,NULL,NULL,'desktop','11301030','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11301050','link','蓝湖',NULL,'https://lanhuapp.com/',NULL,NULL,NULL,NULL,NULL,'desktop','11301040','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11302000','label','',NULL,NULL,NULL,'link-two',NULL,NULL,NULL,'desktop',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11302010','link','豆包',NULL,'https://www.doubao.com/chat/',NULL,NULL,NULL,NULL,NULL,'desktop','11302010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11302011','link','vue3',NULL,'https://cn.vuejs.org/guide/introduction',NULL,NULL,NULL,NULL,NULL,'desktop','11302010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11302020','link','百度AI',NULL,'https://chat.baidu.com/search',NULL,NULL,NULL,NULL,NULL,'desktop','11302020','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11302021','link','elui',NULL,'https://element-plus.org/zh-CN/component/overview.html',NULL,NULL,NULL,NULL,NULL,'desktop','11302020','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11302030','link','DeepSeek',NULL,'https://chat.deepseek.com/',NULL,NULL,NULL,NULL,NULL,'desktop','11302030','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('11302031','link','angular',NULL,'http://www.runoob.com/angularjs/angularjs-reference.html',NULL,NULL,NULL,NULL,NULL,'desktop','11302030','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12101000','label','工作',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'devtools',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12101010','link','个人日报',NULL,'#/home/daynote','个人日报',NULL,NULL,NULL,NULL,'devtools','12101010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12101020','link','团队日报',NULL,'#/home/teamnote','团队日报',NULL,'#1103',NULL,NULL,'devtools','12101010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12101030','link','提测邮件模板',NULL,'#/home/email','邮件模板',NULL,NULL,NULL,NULL,'devtools',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12201000','label','数据库',NULL,NULL,NULL,'link-two',NULL,NULL,NULL,'devtools',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12201010','link','申请数据库授权',NULL,'@dbauth','数据库授权',NULL,NULL,NULL,NULL,'devtools','12201010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12201020','link','我的授权信息',NULL,'@dbmine','我的授权',NULL,NULL,NULL,NULL,'devtools','12201010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12201030','link','SQL表结构管理',NULL,'#/home/dbsql','SQL管理',NULL,NULL,NULL,NULL,'devtools','12201030','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12201040','link','数据库计划任务',NULL,'#/home/dbtask','计划任务',NULL,'#1201',NULL,NULL,'devtools','12201030','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12201050','link','数据库设置',NULL,'#/home/dbset','数据库设置',NULL,'#1201',NULL,NULL,'devtools','12201050','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12201060','link','数据库代理',NULL,'#/home/dbproxy','数据库代理',NULL,'#1201',NULL,NULL,'devtools','12201050','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12201080','link','数据库加解密',NULL,'#/home/dbcode','加解密',NULL,NULL,NULL,NULL,'devtools',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12301000','label','日志',NULL,NULL,NULL,'col-label-h',NULL,NULL,NULL,'devtools',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12301010','link','开发环境日志',NULL,'$/logger/read?logsHost=http://localhost:18232','开发环境日志',NULL,NULL,NULL,NULL,'devtools',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('12301030','link','测试系统日志',NULL,'$/logger/read?logsHost=https://logs.my/','测试系统日志',NULL,NULL,NULL,NULL,'devtools',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13101000','label','PMS',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'devenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13101010','link','{{前端 pms-web}} [http://web.pms.my]',NULL,'http://web.pms.my',NULL,NULL,NULL,NULL,NULL,'devenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13101020','new-line','空行',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'devenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13101050','link','{{管理端 pms-ops}} [http://ops.pms.my]',NULL,'http://ops.pms.my',NULL,NULL,NULL,NULL,NULL,'devenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13101060','link','{{商户系统 pms-business}}[http://buss.pms.my]',NULL,'http://buss.pms.my',NULL,NULL,NULL,NULL,NULL,'devenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13101070','link','{{基础数据 pms-basedata}}[http://data.pms.my]',NULL,'http://data.pms.my',NULL,NULL,NULL,NULL,NULL,'devenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13101080','link','{{短信平台 pms-sms}} [http://sms.pms.my]',NULL,'http://sms.pms.my',NULL,NULL,NULL,NULL,NULL,'devenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13201000','label','积分商城',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'devenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13201010','link','管理后台',NULL,'https://web.shop.my/oweb',NULL,NULL,NULL,NULL,NULL,'devenv','13201010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13201020','link','客户 H5',NULL,'https://app.shop.my/shop',NULL,NULL,NULL,NULL,NULL,'devenv','13201010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13201030','link','shop-ops 后台',NULL,'https://www.shop.my/ops/aq',NULL,NULL,NULL,NULL,NULL,'devenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13201040','link','shop-bdc 后台',NULL,'https://www.shop.my/bdc/aq',NULL,NULL,NULL,NULL,NULL,'devenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('13201050','link','shop-mkt 后台',NULL,'https://www.shop.my/mkt/aq',NULL,NULL,NULL,NULL,NULL,'devenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('14101000','label-cpt','faced',NULL,NULL,NULL,'corpenv-faced',NULL,NULL,'{\"cpt\":\"<span class=\'col-label-h\'><span class=\'col-label-h1\'>faced</span><span class=\'col-label-h2\'>提供给第三方的必须是域名！</span></span>\"}','corpenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('14101010','link','{{http域名}} [http://test.faced.my/gw]',NULL,'http://test.faced.my/gw',NULL,NULL,NULL,'<span class=\'fred\'>https域名 [https://test.faced.my/gw]</span>',NULL,'corpenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('14101020','new-line','空行',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'corpenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('14101030','text','<p>faced的实例在测试环境112</p><p>调用的商城系统部署在67上</p>',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'corpenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('14101040','text','入网IP [111.111.111.112]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'corpenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('14201000','label','企业对接 OA',NULL,NULL,NULL,NULL,NULL,NULL,'{\"cpt\":\"<span class=\'col-label-h\'><span class=\'col-label-h1\'>faced</span><span class=\'col-label-h2\'>提供给第三方的必须是域名！</span>\"}','corpenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('14201010','link','{{前端 pms-web}} [http://web.pms.my]',NULL,'http://web.pms.my',NULL,NULL,NULL,'实际地址为 [192.168.12.67:8962]',NULL,'corpenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('14201020','new-line','空行',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'corpenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('14201030','link','{{后台}} [http://ops.pms.my]',NULL,'http://ops.pms.my',NULL,NULL,NULL,'实际地址为 [192.168.12.67:8075]',NULL,'corpenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('14201040','new-line','空行',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'corpenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('14201050','text','出网IP [111.111.111.110]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'corpenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16101000','label','PMS',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16101010','link','{{前端 pms-web}} [https://web.pms.my]',NULL,'https://web.pms.my',NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16101020','link-more','管理端 pms-ops',NULL,'https://ops.pms.my',NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('1610102001','link','[https://ops.pms.my]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'---',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('1610102002','link','pms1 {{pms-ops}}',NULL,'http://ops.pms.my:18021/info',NULL,NULL,NULL,NULL,NULL,'---',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16101030','link-more','商户系统 pms-business',NULL,'https://buss.pms.my',NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('1610103001','link','[https://buss.pms.my]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'---',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('1610103002','link','pms1 {{pms-business}}',NULL,'http://buss.pms.my:18022/info',NULL,NULL,NULL,NULL,NULL,'---',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('1610103003','link','pms2 {{pms-business}}',NULL,'http://buss.pms.my:28022/info',NULL,NULL,NULL,NULL,NULL,'---',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16101040','link-more','基础数据 pms-basedata',NULL,'https://data.pms.my',NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('1610104001','link','[https://data.pms.my]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'---',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('1610104002','link','pms1 {{pms-basedata}}',NULL,'http://data.pms.my:18023/info',NULL,NULL,NULL,NULL,NULL,'---',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('1610104003','link','pms2 {{pms-basedata}}',NULL,'http://data.pms.my:28023/info',NULL,NULL,NULL,NULL,NULL,'---',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16101060','link','用户中心系统',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16101590','link-more','短信平台 pms-sms',NULL,'https://sms.pms.my',NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('1610159001','link','[https://sms.pms.my]',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'---',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('1610159002','link','{{pms1}}',NULL,'http://sms.pms.my:18040/info',NULL,NULL,NULL,NULL,NULL,'---',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('1610159003','link','{{pms2}}',NULL,'http://sms.pms.my:28040/info',NULL,NULL,NULL,NULL,NULL,'---',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16102000','label','中间件',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16102010','link','nacos',NULL,'http://nacos.my:9848/nacos/',NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16102020','link','{{rabbitmq}} [bugcat/bugcat@369]',NULL,'http://rmq.my:17655/',NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16102030','link','{{网关系统}} [https://gw.my/]',NULL,'https://gw.my/info',NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16304000','label','积分商城',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16304010','link','管理后台',NULL,'https://web.shop.my/oweb',NULL,NULL,NULL,NULL,NULL,'prodenv','16304010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16304020','link','客户 H5',NULL,'https://app.shop.my/shop',NULL,NULL,NULL,NULL,NULL,'prodenv','16304010','1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16304030','link','shop-ops 后台',NULL,'https://www.shop.my/ops/aq',NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16304040','link','shop-bdc 后台',NULL,'https://www.shop.my/bdc/aq',NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('16304050','link','shop-mkt 后台',NULL,'https://www.shop.my/mkt/aq',NULL,NULL,NULL,NULL,NULL,'prodenv',NULL,'1');
insert into `bc_link` (`id`, `type`, `label`, `title`, `linkUrl`, `winTitle`, `className`, `acs`, `desc`, `extra`, `pageId`, `rowId`, `active`) values('17101000','label','积分商城',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'localenv',NULL,'1');



-- 权限基础信息
INSERT INTO `zt_group`(`name`, `role`, `desc`) VALUES('#团队日报', 'admin', '#1103');
INSERT INTO `zt_group`(`name`, `role`, `desc`) VALUES('#数据库DBA', 'admin', '#1201');
INSERT INTO `zt_group`(`name`, `role`, `desc`) VALUES('#sonar管理员', 'admin', '#1301');
INSERT INTO `zt_group`(`name`, `role`, `desc`) VALUES('#后台组', 'admin', '#3100');

-- 默认权限
INSERT INTO `zt_usergroup`(`account`, `group`)
SELECT 'admin' AS 'account', g.id FROM `zt_group` g WHERE g.desc LIKE '#%';