package sqlnote.rest.dbconfig

import groovy.json.JsonSlurper

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.RepositoryFactory
import sqlnote.db.DataSourceCache
import sqlnote.db.SystemDataSource
import sqlnote.domain.DataSourceConfiguration

class PostDataSource {
    private static final Logger logger = LoggerFactory.getLogger(PostDataSource.class)
    
    void execute(requestText) {
        DataSourceConfiguration config = this.makeDataSourceConfiguration(requestText)
        this.saveDataSource(config)
        DataSourceCache.verifyConnectivity(config.id)
    }
    
    private DataSourceConfiguration makeDataSourceConfiguration(requestText) {
        def data = new JsonSlurper().parseText(requestText)
        
        DataSourceConfiguration config = new DataSourceConfiguration()
        
        config.with {
            name = data.name
            driver = data.driver
            url = data.url
            userName = data.userName
            password = data.password
        }
        
        config
    }

    private void saveDataSource(DataSourceConfiguration config) {
        SystemDataSource.withTransaction { db ->
            RepositoryFactory.getDataSourceConfigurationRepository(db).register(config)
            DataSourceCache.put(config.id, config)
        }
    }
}
