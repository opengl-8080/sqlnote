package sqlnote.db

import java.sql.SQLException

import javax.sql.DataSource

import org.apache.commons.dbcp2.BasicDataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.domain.DataSourceConfiguration
import sqlnote.domain.UnConnectableDatabaseException
import sqlnote.rest.dbconfig.PutDataSource

class DataSourceUtil {
    private static final Logger logger = LoggerFactory.getLogger(PutDataSource.class)
    
    public static DataSource build(DataSourceConfiguration config) {
        build(config.driver, config.url, config.userName, config.password)
    }
    
    public static DataSource build(String driver, String url, String user, String password) {
        BasicDataSource dataSource = new BasicDataSource()
        dataSource.setDriverClassName(driver)
        dataSource.setUrl(url)
        dataSource.setUsername(user)
        dataSource.setPassword(password)
        
        dataSource
    }
    
    public static void closeDataSource(DataSource ds) {
        BasicDataSource bds = ds as BasicDataSource
        bds.close()
    }
    
    public static String getDetailStatus(DataSource ds) {
        BasicDataSource bds = ds as BasicDataSource
        
        """\
        |driver=${bds.driverClassName}
        |url=${bds.url}
        |user=${bds.username}
        |idle=${bds.numIdle}
        |active=${bds.numActive}
        |closed?=${bds.closed}""".stripMargin()
    }

}
