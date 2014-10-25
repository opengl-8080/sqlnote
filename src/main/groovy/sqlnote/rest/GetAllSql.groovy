package sqlnote.rest

import java.util.function.Function;

import groovy.json.JsonBuilder;
import sqlnote.db.SystemDataSource;
import sqlnote.domain.SqlNote;
import sqlnote.domain.SqlNoteRepository;

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
