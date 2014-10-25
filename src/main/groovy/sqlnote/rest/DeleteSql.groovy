package sqlnote.rest

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.db.SystemDataSource
import sqlnote.domain.SqlNoteRepository;

class DeleteSql {
    private static final Logger logger = LoggerFactory.getLogger(DeleteSql.class)
    
    void execute(long id) {
        SystemDataSource.withTransaction { db ->
            SqlNoteRepository repository = new SqlNoteRepository(db)
            
            repository.findById(id) // if not found then throw exception
            repository.remove(id)
            
            logger.info('delete sql id={}', id)
        }
    }
}
