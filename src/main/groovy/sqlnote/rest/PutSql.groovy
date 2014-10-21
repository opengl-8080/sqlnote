package sqlnote.rest

import sqlnote.SqlNote;
import sqlnote.db.SqlNoteRepository;
import groovy.json.JsonSlurper

class PutSql {
    
    void execute(long id, String requestBody) {
        def json = new JsonSlurper().parseText(requestBody)
        
        SqlNoteRepository repository = new SqlNoteRepository()
        SqlNote note = repository.findById(id)
        
        note.title = json.title
        note.sqlTemplate = json.sql
        note.parameterNames = json.parameterNames
        
        repository.modify(note)
    }
}
