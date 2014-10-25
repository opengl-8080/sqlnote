package sqlnote.rest

import groovy.json.JsonBuilder
import sqlnote.db.SystemDataSource;
import sqlnote.domain.SqlNote;
import sqlnote.domain.SqlNoteRepository;

class GetSqlDetail {
    
    String execute(long sqlId) {
        def json = new JsonBuilder()
        
        SystemDataSource.withTransaction { db ->
            SqlNote note = new SqlNoteRepository(db).findById(sqlId)
            
            json {
                id note.id
                title note.title
                sql note.sqlTemplate
                parameters note.parameters.collect {
                    [
                        name: it.name,
                        type: it.dataType
                    ]
                }
                executeSqlUrl UrlBuilder.buildExecuteSqlUrl(note.id)
            }
        }
        
        json.toString()
    }
}
