package cc.bugcat.bcdt.ops.openapi.dbsql.list;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.acore.framework.utils.DateUtil;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.model.zentao.BcSql;
import cc.bugcat.bcdt.ops.openapi.dbsql.list.vi.DbsqlListVi;
import cc.bugcat.bcdt.ops.openapi.dbsql.list.vo.DbsqlListVo;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.service.inter.BcSqlService;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class DbsqlListService extends AbstractOpenApiService<DbsqlListVi, List<DbsqlListVo>> {
    
    @Autowired
    private BcSqlService sqlService;
    
    @Override
    @Transactional(readOnly = true)
    public List<DbsqlListVo> execute(DbsqlListVi reqVi, RequestContext context) {
        UserInfo user = UserUtil.getUser(context);
        
        BcSql query = new BcSql();
        if( StringUtils.isNotBlank(reqVi.getId()) ){
            List<Long> ids = new ArrayList<>();
            Pattern pattern = Pattern.compile("(\\d+)");
            Matcher matcher = pattern.matcher(reqVi.getId());
            while ( matcher.find() ) {
                try {
                    ids.add(Long.parseLong(matcher.group(1)));
                } catch ( NumberFormatException ex ) {
                    throw BusinessException.builder(Errors.ERROR_PARAM, reqVi.getId() + " 不是规范的主键Id！");
                }
            }
            query.setIds(ids);
        } else {
            Date startDate = DateUtil.rd.startDateTime(reqVi.getStartDate());
            Date endDate = DateUtil.rd.endDateTime(reqVi.getEndDate());
            query.setStartDate(startDate);
            query.setEndDate(endDate);
            query.setDbId(reqVi.getDbId());
            query.setStatus(reqVi.getStatus());
            query.setDesc(reqVi.getDesc());
            if( reqVi.isOnlyMe() ){
                query.setAccount(user.getAccount());
            }
        }
        
        List<BcSql> list = sqlService.list(MyWrapper.of(BcSql.class)
                        .in(BcSql::getId, query.getIds())
                        .eq(query::getAccount)
                        .eq(query::getDbId)
                        .like(query::getDesc)
                        .eq(query::getStatus)
                        .ge(BcSql::getCreateDate, query.getStartDate())
                        .le(BcSql::getCreateDate, query.getEndDate())
                        .orderByAsc(BcSql::getId)
                );
        
        boolean dba = Opers.hasOper(user, Opers.dba);
        
        List<DbsqlListVo> result = Optional.ofNullable(list).orElseGet(ArrayList::new)
                .stream()
                .map(sql -> {
                    DbsqlListVo vo = BeanCopyUtil.copy(sql, DbsqlListVo::new);
                    if ( dba && "10".equals(sql.getStatus()) ){ //未同步，
                        vo.setCanSubmit("1");
                    }
                    return vo;
                })
                .collect(Collectors.toList());
        
        return result;
        
    }
}
