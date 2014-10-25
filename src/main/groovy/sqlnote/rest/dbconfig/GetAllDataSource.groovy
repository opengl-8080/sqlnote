package sqlnote.rest.dbconfig

import groovy.json.JsonBuilder
import sqlnote.db.SystemDataSource
import sqlnote.domain.DataSourceConfigurationRepository
import sqlnote.rest.UrlBuilder

class GetAllDataSource {
    
    String execute() {
        def json = new JsonBuilder()
        
        SystemDataSource.with {db ->
            def repo = new DataSourceConfigurationRepository(db)
            
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
