package sqlnote.rest.dbconfig

import groovy.json.JsonBuilder
import sqlnote.RepositoryFactory
import sqlnote.db.SystemDataSource
import sqlnote.domain.DataSourceConfiguration

class GetDataSourceDetail {
    
    String execute(long dsId) {
        def json = new JsonBuilder()
        
        SystemDataSource.with {db ->
            def repo = RepositoryFactory.getDataSourceConfigurationRepository(db)
            
            DataSourceConfiguration conf = repo.findById(dsId)
            
            json {
                id conf.id
                name conf.name
                driver conf.driver
                url conf.url
                userName conf.userName
                password conf.password
            }
        }
        
        json.toString()
    }
}
