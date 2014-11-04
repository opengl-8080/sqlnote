package sqlnote

import sqlnote.db.DataSourceConfigurationRepositoryImpl
import sqlnote.db.DatabaseAccess
import sqlnote.domain.DataSourceConfigurationRepository

class RepositoryFactory {
    
    static DataSourceConfigurationRepository getDataSourceConfigurationRepository(DatabaseAccess db) {
        return new DataSourceConfigurationRepositoryImpl(db);
    }
}
