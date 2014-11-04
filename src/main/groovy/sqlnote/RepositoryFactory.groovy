package sqlnote

import sqlnote.db.DataSourceConfigurationRepositoryImpl
import sqlnote.db.DatabaseAccess
import sqlnote.db.ExternalDataRepositoryImpl
import sqlnote.db.SqlNoteRepositoryImpl
import sqlnote.domain.DataSourceConfigurationRepository
import sqlnote.domain.ExternalDataRepository
import sqlnote.domain.SqlNoteRepository

class RepositoryFactory {
    
    static DataSourceConfigurationRepository getDataSourceConfigurationRepository(DatabaseAccess db) {
        return new DataSourceConfigurationRepositoryImpl(db);
    }
    
    static ExternalDataRepository getExternalDataRepository() {
        return new ExternalDataRepositoryImpl();
    }
    
    static SqlNoteRepository getSqlNoteRepository(DatabaseAccess db) {
        return new SqlNoteRepositoryImpl(db);
    }
}
