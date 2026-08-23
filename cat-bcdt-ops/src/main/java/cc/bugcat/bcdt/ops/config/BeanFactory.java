package cc.bugcat.bcdt.ops.config;


import cc.bugcat.acore.framework.constants.Const;
import cc.bugcat.acore.framework.spi.IStringCrypto;
import cc.bugcat.acore.framework.spi.IUserInfo;
import cc.bugcat.acore.webmvc.spi.IUserInfoService;
import cc.bugcat.bcdt.ops.openapi.dbEncry.DbEncryptService;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.service.mysql.spi.IDatabaseProxy;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;


/**
 * 启动时加载一些资源
 * @author bugcat-cc
 */
@Configuration
public class BeanFactory {


    private static final Log logger = LogFactory.getLog(BeanFactory.class);

    
    @Autowired
    private AppConfig appConfig;

    
//    @Bean
//    @ConditionalOnMissingBean
//    public IUserInfoService userCacheService() {
//        return new IUserInfoService() {
//            @Override
//            public IUserInfo preUserInfo(HttpServletRequest request) {
//                UserInfo userInfo = UserUtil.defaultUser();
//                request.setAttribute(Const.userToken, userInfo);
//                return userInfo;
//            }
//        };
//    }
    

    @Bean
    @ConditionalOnProperty(value = "mysql.proxy.enable", havingValue = "false", matchIfMissing = true)
    public IDatabaseProxy mysqlProxy() {
        return new IDatabaseProxy() {};
    }
    
    
    
    /**
     * 数据库字段加解密；
     * */
    @Bean
    public IStringCrypto stringCrypto() {
        String key = appConfig.getCryptoKey(); //System.getProperty("db-crypto.key");
        Function<String, String> encrypt = DbEncryptService.getHandler("encrypt", "sm4", key);
        Function<String, String> decrypt = DbEncryptService.getHandler("decrypt", "sm4", key);
        return new IStringCrypto() {
            @Override
            public String encode(String value) {
                return value == null ? null : encrypt.apply(value);
            }
            @Override
            public String decode(String value) {
                return value == null ? null : decrypt.apply(value);
            }
        };
    }
     
}
