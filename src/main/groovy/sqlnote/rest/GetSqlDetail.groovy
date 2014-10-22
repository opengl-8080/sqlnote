package sqlnote.rest

import groovy.json.JsonBuilder
import sqlnote.SqlNote
import sqlnote.db.SqlNoteRepository

class GetSqlDetail {
    
    String execute(long sqlId) {
        SqlNote note = new SqlNoteRepository().findById(sqlId)
        
        def json = new JsonBuilder()
        
        json {
            id note.id
            title note.title
            sql note.sqlTemplate
            parameterNames note.parameterNames.collect {
                [
                    name: it.name,
                    type: it.dataType
                ]
            }
            executeSqlUrl UrlBuilder.buildExecuteSqlUrl(note.id)
        }
        
        json.toString()
    }
}
