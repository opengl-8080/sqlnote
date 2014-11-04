package sqlnote.rest.sql

import sqlnote.RepositoryFactory
import sqlnote.db.SystemDataSource

class CopySql {
    
    void execute(long id) {
        SystemDataSource.withTransaction { db ->
            def repository = RepositoryFactory.getSqlNoteRepository(db)
            
            def note = repository.findById(id)
            
            repository.register(note.copy())
        }
    }
}
