package cc.bugcat.bcdt.ops.utils;


import cc.bugcat.acore.mariadb.config.DatabaseConfigurer;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.bcdt.ops.model.zentao.BcDbSet;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 *
 * @author bugcat-cc
 */
public class JDBCUtil {


	public static Connection getConn(BcDbSet dbSet) {
		try {
			if ( StringUtils.isNotBlank(dbSet.getDriverName()) ) {
				JDBCUtil.class.getClassLoader().loadClass(dbSet.getDriverName());
			}
			String jdbcUrl = DatabaseConfigurer.getJdbcUrl(dbSet.getJdbcUrl());
			return DriverManager.getConnection(jdbcUrl, dbSet.getUsername(), dbSet.getPassword());
		} catch (Exception ex) {
			throw BusinessException.builder("500", ex);
		}
	}
	
	public static void close(Connection conn){
		try {
			if( conn != null && !conn.isClosed() ){
				conn.close();
			}
		} catch (SQLException e) {
			throw BusinessException.builder("500", e);
		}
	}

}
