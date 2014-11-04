package sqlnote.rest.dbconfig

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.RepositoryFactory
import sqlnote.db.DataSourceCache
import sqlnote.db.SystemDataSource

class DeleteDataSource {
    private static final Logger logger = LoggerFactory.getLogger(DeleteDataSource.class)
    
    void execute(long id) {
        SystemDataSource.withTransaction { db ->
            RepositoryFactory.getDataSourceConfigurationRepository(db).with {
                findById(id) // if not found throw exception
                remove(id)
            }
            
            logger.info("delete datasource configuration from db. id=${id}")
            
            DataSourceCache.remove(id)
            logger.info("remove datasource configuration from cache. id=${id}")
        }
    }
}
