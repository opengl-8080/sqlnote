package sqlnote.rest.sql

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.RepositoryFactory
import sqlnote.db.SystemDataSource

class DeleteSql {
    private static final Logger logger = LoggerFactory.getLogger(DeleteSql.class)
    
    void execute(long id) {
        SystemDataSource.withTransaction { db ->
            def repository = RepositoryFactory.getSqlNoteRepository(db)
            
            repository.findById(id) // if not found then throw exception
            repository.remove(id)
            
            logger.info('delete sql id={}', id)
        }
    }
}
