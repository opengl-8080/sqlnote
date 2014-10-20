package sqlnote.rest

import java.util.function.Function;

import groovy.json.JsonBuilder;
import sqlnote.SqlNote
import sqlnote.db.SqlNoteRepository

class GetAllSql {
    
    String execute(Function<Long, String> urlBuilder) {
        List<SqlNote> list = new SqlNoteRepository().findAll()
        
        JsonBuilder json = new JsonBuilder()
        
        json(list.collect { sqlNote ->
            json {
                id sqlNote.id
                title sqlNote.title
                url urlBuilder.apply(sqlNote.id)
            }
        })
        
        json.toString()
    }
}
