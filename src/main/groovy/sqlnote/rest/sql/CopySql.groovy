package sqlnote.rest.sql

import sqlnote.db.SystemDataSource
import sqlnote.domain.SqlNoteRepository

class CopySql {
    
    void execute(long id) {
        SystemDataSource.withTransaction { db ->
            def repo = new SqlNoteRepository(db)
            
            def note = repo.findById(id)
            
            repo.register(note.copy())
        }
    }
}
