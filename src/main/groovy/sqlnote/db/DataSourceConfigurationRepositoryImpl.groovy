package sqlnote.db

import java.util.List;

import sqlnote.domain.DataSourceConfiguration;
import sqlnote.domain.DataSourceConfigurationRepository;

class DataSourceConfigurationRepositoryImpl implements DataSourceConfigurationRepository {
    
    DatabaseAccess db
    
    public DataSourceConfigurationRepositoryImpl(DatabaseAccess db) {
        this.db = db
    }

    @Override
    public List<DataSourceConfiguration> findAll() {
        this.db.collect("SELECT * FROM DATA_SOURCE_CONFIGURATION ORDER BY ID ASC", this.&toEntity)
    }
    
    @Override
    public DataSourceConfiguration findById(long id) {
        this.db.firstRow("SELECT * FROM DATA_SOURCE_CONFIGURATION WHERE ID=${id}") { row ->
            if (row) {
                this.toEntity(row)
            } else {
                throw new DataSourceConfigurationNotFoundException(id)
            }
        }
    }
    
    private DataSourceConfiguration toEntity(result) {
        DataSourceConfiguration conf = new DataSourceConfiguration()
        
        conf.with {
            id = result.ID
            name = result.NAME
            driver = result.DRIVER
            url = result.URL
            userName = result.USER_NAME
            password = result.PASSWORD
        }
        
        conf
    }
    
    @Override
    public void register(DataSourceConfiguration config) {
        config.id = this.db.insertSingle("""
            INSERT INTO DATA_SOURCE_CONFIGURATION (NAME, DRIVER, URL, USER_NAME, PASSWORD)
            VALUES (${config.name}, ${config.driver}, ${config.url}, ${config.userName}, ${config.password})
        """)
    }
    
    @Override
    public void modify(DataSourceConfiguration config) {
        this.db.update("""
            UPDATE DATA_SOURCE_CONFIGURATION
               SET NAME=${config.name},
                   DRIVER=${config.driver},
                   URL=${config.url},
                   USER_NAME=${config.userName},
                   PASSWORD=${config.password}
             WHERE ID=${config.id}
        """)
    }
    
    @Override
    public void remove(long id) {
        this.db.delete("DELETE FROM DATA_SOURCE_CONFIGURATION WHERE ID=${id}")
    }
}
