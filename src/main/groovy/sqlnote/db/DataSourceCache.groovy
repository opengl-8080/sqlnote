package sqlnote.db

import java.sql.Connection
import java.sql.SQLException
import java.util.concurrent.ConcurrentHashMap

import javax.sql.DataSource

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.domain.DataSourceConfiguration;
import sqlnote.domain.UnConnectableDatabaseException

class DataSourceCache {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceCache.class);
    private static Map<Long, DataSource> dataSourceCache = new ConcurrentHashMap<>()
    
    synchronized static void put(Long key, DataSourceConfiguration config) {
        if (dataSourceCache.containsKey(key)) {
            DataSource ds = DataSourceCache.get(key)
            DataSourceUtil.closeDataSource(ds)
        }
        
        DataSource ds = DataSourceUtil.build(config)
        this.dataSourceCache.put(key, ds)
    }
    
    static DataSource get(Long key) {
        this.dataSourceCache.get(key)
    }
    
    synchronized static void remove(Long key) {
        DataSource ds = get(key)
        
        this.dataSourceCache.remove(key)
        
        DataSourceUtil.closeDataSource(ds)
    }
    
    static void dump() {
        def sb = new StringBuilder('***** DataSource Cache Dump *****')
        
        dataSourceCache.each { key, ds ->
            sb << """
            |--------------------------------------------
            |key=${key}
            |${DataSourceUtil.getDetailStatus(ds)}
            |--------------------------------------------
            |""".stripMargin()
        }
        
        logger.info sb.toString()
    }
    
    static void verifyConnectivity(long id) {
        try {
            Connection newConnection = get(id).getConnection()
            
            try {
                newConnection.metaData.databaseProductName
            } finally {
                newConnection.close()
            }
            logger.debug('connectable datasource')
        } catch (SQLException e) {
            logger.warn('unconnectable datasource config', e)
            throw new UnConnectableDatabaseException(e.message)
        }
    }
}
