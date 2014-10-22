package sqlnote.rest

import groovy.json.JsonSlurper
import sqlnote.DataType
import sqlnote.SqlNote
import sqlnote.SqlParameter
import sqlnote.db.SqlNoteRepository

class PutSql {
    
    void execute(long id, String requestBody) {
        def json = new JsonSlurper().parseText(requestBody)
        
        SqlNoteRepository repository = new SqlNoteRepository()
        SqlNote note = repository.findById(id)
        
        note.title = json.title
        note.sqlTemplate = json.sql
        note.parameters = json.parameters.collect {new SqlParameter(it.name, DataType.valueOf(it.type))}
        
        repository.modify(note)
    }
}
