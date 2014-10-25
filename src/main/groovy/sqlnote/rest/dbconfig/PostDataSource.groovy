package sqlnote.rest.dbconfig

import groovy.json.JsonSlurper

import java.sql.SQLException

import javax.sql.DataSource

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.db.DataSourceUtil
import sqlnote.db.DataSourceCache
import sqlnote.db.SystemDataSource
import sqlnote.domain.DataSourceConfiguration
import sqlnote.domain.DataSourceConfigurationRepository
import sqlnote.domain.UnConnectableDatabaseException

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
            new DataSourceConfigurationRepository(db).register(config)
            DataSourceCache.put(config.id, config)
        }
    }
}
