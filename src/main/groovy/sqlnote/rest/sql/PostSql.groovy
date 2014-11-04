package sqlnote.rest.sql

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.RepositoryFactory
import sqlnote.db.SystemDataSource
import sqlnote.domain.SqlNote

class PostSql {
    private static final Logger logger = LoggerFactory.getLogger(PostSql.class)
    
    void execute() {
        SystemDataSource.withTransaction { db ->
            SqlNote note = new SqlNote()
            RepositoryFactory.getSqlNoteRepository(db).register(note)
            
            logger.info('post sql')
        }
    }
}
