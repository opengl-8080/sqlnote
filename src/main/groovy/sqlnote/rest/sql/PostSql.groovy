package sqlnote.rest.sql

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.db.SystemDataSource
import sqlnote.domain.SqlNote;
import sqlnote.domain.SqlNoteRepository;

class PostSql {
    private static final Logger logger = LoggerFactory.getLogger(PostSql.class)
    
    void execute() {
        SystemDataSource.withTransaction { db ->
            SqlNote note = new SqlNote()
            new SqlNoteRepository(db).register(note)
            
            logger.info('post sql')
        }
    }
}
