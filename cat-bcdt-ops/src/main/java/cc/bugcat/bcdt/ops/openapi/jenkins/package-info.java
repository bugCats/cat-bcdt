package cc.bugcat.bcdt.ops.openapi.jenkins;

/**
 * 
 * 
 *  办公平台集成了快速切换Jenkins项目分支的功能，这需要办公平台与Jenkins安装在同服务器上。
 *  
 *  若两者不在同一服务器上（修改比较复杂，强烈建议都部署在一台服务器），进入办公平台源码‘src/main/three’目录中：
 *  
 *      1. 新增一个Tomcat启动钩子对象，手动将‘cc.bugcat.jinkens.plugin.ctrl.JenkinsCtrl’类作为Controller注册到容器中；
 * 
 *      2. 将‘cc/bugcat/jinkens’单独发布为Jar包；
 * 
 *      3. 修改Jenkins启动命令，添加‘-Dloader.path=上述Jar地址’；
 * 
 *      4. 修改办公平台配置文件中‘jenkins.host’值。
 * 
 * 
 *  这样在办公平台上操作‘分支管理’，在修改分支后Jenkins才会生效！
 * 
 * 
 * 
 * */