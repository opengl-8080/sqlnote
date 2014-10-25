package sqlnote.rest

import groovy.json.JsonSlurper

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.DataType
import sqlnote.SqlNote
import sqlnote.SqlParameter
import sqlnote.db.SqlNoteRepository
import sqlnote.db.SystemDataSource

class PutSql {
    private static final Logger logger = LoggerFactory.getLogger(PutSql.class)
    
    void execute(long id, String requestBody) {
        SystemDataSource.withTransaction { db ->
            def json = new JsonSlurper().parseText(requestBody)
            
            SqlNoteRepository repository = new SqlNoteRepository(db)
            SqlNote note = repository.findById(id)
            
            note.title = json.title
            note.sqlTemplate = json.sql
            note.parameters = json.parameters.collect {new SqlParameter(it.name, DataType.valueOf(it.type))}
            
            repository.modify(note)
            
            logger.info('put sql id={}', note.id)
        }
    }
}
