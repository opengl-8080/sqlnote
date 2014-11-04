package sqlnote

import sqlnote.db.DataSourceConfigurationRepositoryImpl
import sqlnote.db.DatabaseAccess
import sqlnote.db.ExternalDataRepositoryImpl
import sqlnote.db.SqlNoteRepositoryImpl
import sqlnote.db.SystemConfigurationRepositoryImpl;
import sqlnote.domain.DataSourceConfigurationRepository
import sqlnote.domain.query.ExternalDataRepository;
import sqlnote.domain.sql.SqlNoteRepository;
import sqlnote.domain.system.SystemConfigurationRepository;

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
    
    static SystemConfigurationRepository getSystemConfigurationRepository(DatabaseAccess db) {
        return new SystemConfigurationRepositoryImpl(db)
    }
}
