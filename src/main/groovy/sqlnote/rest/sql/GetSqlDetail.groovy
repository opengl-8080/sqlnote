package sqlnote.rest.sql

import groovy.json.JsonBuilder
import sqlnote.RepositoryFactory
import sqlnote.db.SystemDataSource
import sqlnote.domain.sql.SqlNote;
import sqlnote.rest.UrlBuilder

class GetSqlDetail {
    
    String execute(long sqlId) {
        def json = new JsonBuilder()
        
        SystemDataSource.withTransaction { db ->
            SqlNote note = RepositoryFactory.getSqlNoteRepository(db).findById(sqlId)
            
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
