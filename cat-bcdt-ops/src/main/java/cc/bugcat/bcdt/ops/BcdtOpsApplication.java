package cc.bugcat.bcdt.ops;


import cc.bugcat.acore.mybatis.annotation.EnableEncrypt;
import cc.bugcat.acore.mybatis.annotation.EnableMapperProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 
 * @author bugcat-cc
 * */
@EnableEncrypt //开启数据库字段加解密
@EnableMapperProxy //开启Mapper二次代理
@EnableScheduling //启用定时任务
@SpringBootApplication(scanBasePackages = "cc.bugcat")
@MapperScan("cc.bugcat.bcdt.ops.dao")
public class BcdtOpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BcdtOpsApplication.class, args);
    }

}
