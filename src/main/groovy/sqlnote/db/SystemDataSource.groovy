package sqlnote.db

import java.sql.Connection
import java.util.function.Consumer;

import javax.sql.DataSource

import org.apache.commons.dbcp2.BasicDataSource;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
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
        init(resolveUrl())
    }
    
    synchronized static DataSource init(url) {
        if (!SYSTEM_DATASOURCE) {
            SYSTEM_DATASOURCE = DataSourceUtil.build(DRIVER, url ?: resolveUrl(), USER, PASS)
        }
        
        SYSTEM_DATASOURCE
    }
    
    static String resolveUrl() {
        def sqlNoteHome = System.getenv('SQLNOTE_HOME')
        def dbFile
        
        if (sqlNoteHome) {
            dbFile = new File(sqlNoteHome, 'db/sqlnote')
        } else {
            dbFile = new File(System.getProperty('user.home'), '.sqlnote/db/sqlnote')
        }
        
        logger.info("database dir = {}", dbFile.absolutePath)
        
        return "jdbc:hsqldb:file:${dbFile.absolutePath};shutdown=true"
    }
    
    static Connection getConnection() {
        if (!SYSTEM_DATASOURCE) {
            throw new IllegalStateException('DataSource is not initialized.')
        }
        
        return SYSTEM_DATASOURCE.getConnection()
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
