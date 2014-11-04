package sqlnote.rest.sql

import groovy.json.JsonBuilder

import java.util.function.Function

import sqlnote.RepositoryFactory
import sqlnote.db.SystemDataSource
import sqlnote.domain.sql.SqlNote;

class GetAllSql {
    
    String execute(Function<Long, String> urlBuilder) {
        JsonBuilder json = new JsonBuilder()
        
        SystemDataSource.withTransaction { db ->
            List<SqlNote> list = RepositoryFactory.getSqlNoteRepository(db).findAll()
            json(list.collect { sqlNote ->
                return [
                    id: sqlNote.id,
                    title: sqlNote.title,
                    url: urlBuilder.apply(sqlNote.id)
                ]
            })
        }
        
        return json.toString()
    }
}
