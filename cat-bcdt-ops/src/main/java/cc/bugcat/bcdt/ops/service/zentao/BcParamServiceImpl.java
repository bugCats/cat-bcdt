package cc.bugcat.bcdt.ops.service.zentao;


import cc.bugcat.acore.framework.cache.spi.InnerCache;
import cc.bugcat.acore.framework.cache.spi.InnerCacheTags;
import cc.bugcat.acore.mybatis.plus.BaseService;
import cc.bugcat.bcdt.ops.dao.zentao.BcParamDao;
import cc.bugcat.bcdt.ops.model.zentao.BcParam;
import cc.bugcat.bcdt.ops.service.inter.BcParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author auto
 * 2024-04-10 13:59:04
 * */
@Service
public class BcParamServiceImpl extends BaseService<BcParamDao, BcParam> implements BcParamService {


    private final InnerCache cache = InnerCache.getCache("params:");


    
    @Override
    public String getValue(String paramId) {
        return getValue(paramId, null);
    }

    
    
    @Override
    public String getValue(String paramId, String defaultValue){
        String value = cache.getAndRefresh(paramId, InnerCache.hour, String.class);
        if( StringUtils.isNotBlank(value) ){
            return value;
        }
        if( value == null ){
            BcParam param = super.getById(paramId);
            if( param == null ){
                value = "";
            } else {
                value = param.getValue();
            }
            
            cache.set(paramId, InnerCache.hour, InnerCacheTags.of(value));
            return value;
        }
        return defaultValue; 
    }
    
    
}

