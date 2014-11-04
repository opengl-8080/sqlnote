package sqlnote.rest.system

import groovy.json.JsonSlurper
import sqlnote.RepositoryFactory
import sqlnote.db.SystemDataSource
import sqlnote.domain.SystemConfiguration

class PutSystemConfiguration {
    
    void execute(String requestBody) {
        def json = new JsonSlurper().parseText(requestBody)
        
        SystemDataSource.withTransaction { db ->
            def repo = RepositoryFactory.getSystemConfigurationRepository(db)
            
            SystemConfiguration config = new SystemConfigParser().parse(json)
            
            repo.modify(config)
        }
    }
}
