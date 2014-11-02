package sqlnote.db

import java.sql.Connection

import javax.sql.DataSource

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ExternalDataSource {
    private static final Logger logger = LoggerFactory.getLogger(ExternalDataSource.class)
    
    static void with(long dataSourceId, Closure closure) {
        DataSource ds = DataSourceCache.get(dataSourceId)
        
        Connection con = ds.connection
        
        try {
            con.setReadOnly(true)
            closure(buildExternalDatabase(con), new DatabaseAccess(con))
        } finally {
            con.close()
            logger.trace('close database connection')
        }
    }
    
    private static ExternalDatabase buildExternalDatabase(Connection con) {
        def dbName = con.metaData.databaseProductName
        
        if (dbName == 'Oracle') {
            return new OracleDatabase()
        } else if (dbName == 'MySQL') {
            return new MySQLDatabase()
        } else if (dbName.contains('HSQL')) {
            return new HsqlDatabase();
        } else {
            throw new UnSupportedDatabaseException(dbName)
        }
    }
}
