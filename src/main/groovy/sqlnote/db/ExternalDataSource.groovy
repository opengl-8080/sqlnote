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
            closure(new ExternalDatabase(), new DatabaseAccess(con))
        } finally {
            con.close()
            logger.trace('close database connection')
        }
    }
}
