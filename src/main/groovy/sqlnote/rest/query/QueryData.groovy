package sqlnote.rest.query

import sqlnote.domain.ExternalDataRepository
import sqlnote.domain.ResponseWriter

class QueryData {
    
    void execute(long sqlId, long dsId, Map<String, String[]> queryMap, ResponseWriter rw) {
        def condition = queryMap.collectEntries { key, value -> [key, value[0]] }
        
        new ExternalDataRepository().export(sqlId, dsId, condition, rw)
    }
    
}
