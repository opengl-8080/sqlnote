package sqlnote.rest.dbconfig

import groovy.json.JsonSlurper

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.RepositoryFactory
import sqlnote.db.DataSourceCache
import sqlnote.db.SystemDataSource
import sqlnote.domain.DataSourceConfiguration

class PutDataSource {
    private static final Logger logger = LoggerFactory.getLogger(PutDataSource.class)
    
    void execute(long configId, String requestBody) {
        def json = new JsonSlurper().parseText(requestBody)
        
        SystemDataSource.withTransaction { db ->
            def repo = RepositoryFactory.getDataSourceConfigurationRepository(db)
            
            DataSourceConfiguration config = repo.findById(configId)
            
            config.with {
                name = json.name
                driver = json.driver
                url = json.url
                userName = json.userName
                password = json.password
            }
            
            repo.modify(config)
            
            DataSourceCache.put(configId, config)
        }
        
        DataSourceCache.verifyConnectivity(configId)
    }
}
