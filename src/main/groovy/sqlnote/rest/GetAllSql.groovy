package sqlnote.rest

import java.util.function.Function;

import groovy.json.JsonBuilder;
import sqlnote.SqlNote
import sqlnote.db.SqlNoteRepository
import sqlnote.db.SystemDataSource;

class GetAllSql {
    
    String execute(Function<Long, String> urlBuilder) {
        JsonBuilder json = new JsonBuilder()
        
        SystemDataSource.withTransaction { db ->
            List<SqlNote> list = new SqlNoteRepository(db).findAll()
            
            json(list.collect { sqlNote ->
                json {
                    id sqlNote.id
                    title sqlNote.title
                    url urlBuilder.apply(sqlNote.id)
                }
            })
        }
        
        json.toString()
    }
}
