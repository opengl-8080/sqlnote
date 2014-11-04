package sqlnote.rest.dbconfig

import groovy.json.JsonBuilder
import sqlnote.db.DataSourceConfigurationRepositoryImpl;
import sqlnote.db.SystemDataSource
import sqlnote.rest.UrlBuilder

class GetAllDataSource {
    
    String execute() {
        def json = new JsonBuilder()
        
        SystemDataSource.with {db ->
            def repo = new DataSourceConfigurationRepositoryImpl(db)
            
            json(
                repo.findAll().collect { config ->
                    json {
                        id config.id
                        name config.name
                        verifyUrl UrlBuilder.buildVerifyDataSourceUrl(config.id)
                    }
                }
            )
            
        }
        
        json.toString()
    }
}
