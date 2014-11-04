package sqlnote.rest.sql

import groovy.json.JsonSlurper

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.RepositoryFactory
import sqlnote.db.SystemDataSource
import sqlnote.domain.DataType
import sqlnote.domain.SqlNote
import sqlnote.domain.SqlParameter

class PutSql {
    private static final Logger logger = LoggerFactory.getLogger(PutSql.class)
    
    void execute(long id, String requestBody) {
        SystemDataSource.withTransaction { db ->
            def json = new JsonSlurper().parseText(requestBody)
            
            def repository = RepositoryFactory.getSqlNoteRepository(db)
            SqlNote note = repository.findById(id)
            
            note.title = json.title
            note.sqlTemplate = json.sql
            note.parameters = json.parameters.collect {new SqlParameter(it.name, DataType.valueOf(it.type))}
            
            repository.modify(note)
            
            logger.info('put sql id={}', note.id)
        }
    }
}
