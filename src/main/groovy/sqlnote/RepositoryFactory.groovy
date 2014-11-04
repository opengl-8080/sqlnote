package sqlnote

import sqlnote.db.DataSourceConfigurationRepositoryImpl
import sqlnote.db.DatabaseAccess
import sqlnote.db.ExternalDataRepositoryImpl
import sqlnote.domain.DataSourceConfigurationRepository
import sqlnote.domain.ExternalDataRepository

class RepositoryFactory {
    
    static DataSourceConfigurationRepository getDataSourceConfigurationRepository(DatabaseAccess db) {
        return new DataSourceConfigurationRepositoryImpl(db);
    }
    
    static ExternalDataRepository getExternalDataRepository() {
        return new ExternalDataRepositoryImpl();
    }
}
