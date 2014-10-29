package sqlnote.db

import java.sql.Connection
import java.util.function.Consumer;

import javax.sql.DataSource

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SystemDataSource {
    private static final Logger logger = LoggerFactory.getLogger(SystemDataSource.class)
    
    private static final String DRIVER = 'org.hsqldb.jdbcDriver';
    private static final String URL = 'jdbc:hsqldb:file:db/sqlnote;shutdown=true';
    private static final String USER = 'SA';
    private static final String PASS = '';
    
    private static DataSource SYSTEM_DATASOURCE;
    
    static DataSource init() {
        init(URL)
    }
    
    synchronized static DataSource init(url) {
        if (!SYSTEM_DATASOURCE) {
            SYSTEM_DATASOURCE = DataSourceUtil.build(DRIVER, url ?: URL, USER, PASS)
        }
        
        SYSTEM_DATASOURCE
    }
    
    static Connection getConnection() {
        init().getConnection()
    }

    static void withTransaction(Closure closure) {
        Connection con = getConnection()
        try {
            con.setAutoCommit(false)
            closure(new DatabaseAccess(con))
            con.commit()
            logger.trace('commit transaction')
        } catch(e) {
            con.rollback()
            logger.error('rollback transaction', e)
            throw e
        } finally {
            con.close()
            logger.trace('close database connection')
        }
    }
    
    static void with(Closure closure) {
        Connection con = getConnection()
        try {
            closure(new DatabaseAccess(con))
        } finally {
            con.close()
        }
    }
    
    static void with(Consumer<DatabaseAccess> closure) {
        Connection con = getConnection()
        try {
            closure.accept(new DatabaseAccess(con))
        } finally {
            con.close()
        }
    }
}
