package sqlnote.rest

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.SqlNote
import sqlnote.db.SqlNoteRepository
import sqlnote.db.SystemDataSource

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
