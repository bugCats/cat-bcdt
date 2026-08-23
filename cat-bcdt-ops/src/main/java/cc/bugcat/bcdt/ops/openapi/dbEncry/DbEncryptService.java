package cc.bugcat.bcdt.ops.openapi.dbEncry;


import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.utils.sm.SM4Util;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;
import cc.bugcat.bcdt.ops.openapi.dbEncry.vi.DbEncryptVi;
import cc.bugcat.bcdt.ops.openapi.dbEncry.vo.DbEncryptVo;
import cc.bugcat.bcdt.ops.service.inter.BcDbSetService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class DbEncryptService extends AbstractOpenApiService<DbEncryptVi, List<DbEncryptVo>> {
    
    private static final String SM4_PREFIX = "@sm4#";
    private static final int SM4_PREFIX_LENGTH = SM4_PREFIX.length();
    
    
    private static final String[] slps = new String[]{"\r\n", "\n"};
    

    @Autowired
    private BcDbSetService dbSetService;
    
    
    @Override
    public List<DbEncryptVo> execute(DbEncryptVi reqVi, RequestContext context) {

        
        BcDbSet dbSet = dbSetService.getById(reqVi.getDbId());
        if (dbSet == null || StringUtils.isBlank(dbSet.getCryptoKey())) {
            throw BusinessException.builder(Errors.ERROR_PARAM, "错误的数据库配置！");
        }

        //密钥
        final String cryptKey = dbSet.getCryptoKey();
        //加密方式
        final String mode = dbSet.getCryptoType();
        
        final String codingType = reqVi.getCodingType();
        
        
        //需要处理的字符串
        String[] keywords = keywords(StringUtils.trimToEmpty(reqVi.getSourceCode()));
        
        List<DbEncryptVo> result = new ArrayList<>(keywords.length);
        
        Function<String, String> func = getHandler(codingType, mode, cryptKey);
        
        for(String keyword : keywords){
            
            DbEncryptVo info = new DbEncryptVo();
            result.add(info);
            
            info.setSource(keyword);
            if ( keyword == null ) {
                info.setCoding("");
            } else {
                String coding = func.apply(keyword);
                info.setCoding(coding != null ? coding : keyword);
            }
        }
        return result;
    }
    
    
    public static Function<String, String> getHandler(String codingType, String mode, String cryptKey){
        
        if ( "sm4".equals(mode) ) {
            
            if( "encrypt".equals(codingType) ){ //加密
                
                //加密，输出base64密文
                SM4Util encrypt = SM4Util.encrypt(cryptKey).outputCoder(Base64.getEncoder()::encodeToString);
                
                return (keyword) -> {
                    if ( keyword.startsWith(SM4_PREFIX) ) {
                        return keyword;
                    }
                    return SM4_PREFIX + encrypt.coding(keyword);
                };
                
            } else if( "decrypt".equals(codingType) ){ //解密
                
                //解密，输入base64密文
                SM4Util decrypt = SM4Util.decrypt(cryptKey).inputCoder(Base64.getDecoder()::decode);
                
                return (keyword) -> {
                    if ( keyword.startsWith(SM4_PREFIX)  ) {
                        try {
                            return decrypt.coding(keyword.substring(SM4_PREFIX_LENGTH));
                        } catch ( Exception ex ) {
                            return null;
                        }
                    }
                    return keyword;
                };
                
            }
            
        }
        
        return Function.identity();
    }
    
    
    
    
    private static String[] keywords(String reqStr){
        String slp = null;
        for(String sp : slps){
            if( reqStr.contains(sp) ){
                slp = sp;
                break;
            }
        }
        if( slp == null ){
            return new String[]{reqStr};
        } else {
            return StringUtils.split(reqStr, slp);
        }
    }
}
