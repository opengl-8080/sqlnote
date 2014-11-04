package sqlnote.rest.system

import groovy.json.JsonBuilder
import sqlnote.RepositoryFactory
import sqlnote.db.SystemDataSource
import sqlnote.domain.system.SystemConfiguration;

class GetSystemConfiguration {
    
    String execute() {
        SystemConfiguration config;
        
        SystemDataSource.with { db ->
            def repo = RepositoryFactory.getSystemConfigurationRepository(db)
            config = repo.find()
        }
        
        def json = new JsonBuilder()
        json {
            maxRowNum config.maxRowNum
        }
        
        return json
    }
}
