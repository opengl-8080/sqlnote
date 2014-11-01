package sqlnote.rest.system

import groovy.json.JsonSlurper
import sqlnote.db.SystemDataSource
import sqlnote.domain.SystemConfiguration;
import sqlnote.domain.SystemConfigurationRepository

class PutSystemConfiguration {
    
    void execute(String requestBody) {
        def json = new JsonSlurper().parseText(requestBody)
        
        SystemDataSource.withTransaction { db ->
            def repo = new SystemConfigurationRepository(db)
            
            SystemConfiguration config = new SystemConfiguration()
            config.maxRowNum = json.maxRowNum
            
            repo.modify(config)
        }
    }
}
