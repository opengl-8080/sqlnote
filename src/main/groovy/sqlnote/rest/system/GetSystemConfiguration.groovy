package sqlnote.rest.system

import groovy.json.JsonBuilder
import sqlnote.db.SystemDataSource
import sqlnote.domain.SystemConfiguration
import sqlnote.domain.SystemConfigurationRepository

class GetSystemConfiguration {
    
    String execute() {
        SystemConfiguration config;
        
        SystemDataSource.with { db ->
            def SystemConfigurationRepository repo = new SystemConfigurationRepository(db)
            config = repo.find()
        }
        
        def json = new JsonBuilder()
        json {
            maxRowNum config.maxRowNum
        }
        
        return json
    }
}
