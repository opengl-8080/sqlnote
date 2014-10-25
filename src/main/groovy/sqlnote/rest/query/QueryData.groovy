package sqlnote.rest.query

import org.codehaus.groovy.runtime.GStringImpl

import sqlnote.db.SystemDataSource
import sqlnote.domain.ExternalDataRepository
import sqlnote.domain.ResponseWriter;
import sqlnote.domain.SqlNote
import sqlnote.domain.SqlNoteRepository
import sqlnote.domain.TemplateAnalyzer

class QueryData {
    
    void execute(long sqlId, long dsId, Map<String, String[]> queryMap, OutputStream out) {
        def condition = queryMap.collectEntries { key, value -> [key, value[0]] }
        ResponseWriter rw = new ResponseWriter(out)
        
        new ExternalDataRepository().export(sqlId, dsId, condition, rw)
    }
    
}
