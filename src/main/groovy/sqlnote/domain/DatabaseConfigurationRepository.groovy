package sqlnote.domain

import java.util.List;

import sqlnote.db.DatabaseAccess

class DatabaseConfigurationRepository {
    
    DatabaseAccess db
    
    public DatabaseConfigurationRepository(DatabaseAccess db) {
        this.db = db
    }

    public List<DatabaseConfiguration> findAll() {
        this.db.collect("SELECT * FROM DATABASE_CONFIGURATION ORDER BY ID ASC", this.&toEntity)
    }

    public DatabaseConfiguration findById(long id) {
        this.db.firstRow("SELECT * FROM DATABASE_CONFIGURATION WHERE ID=${id}", this.&toEntity)
    }
    
    private DatabaseConfiguration toEntity(result) {
        DatabaseConfiguration conf = new DatabaseConfiguration()
        
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

    public void register(DatabaseConfiguration config) {
        config.id = this.db.insertSingle("""
            INSERT INTO DATABASE_CONFIGURATION (NAME, DRIVER, URL, USER_NAME, PASSWORD)
            VALUES (${config.name}, ${config.driver}, ${config.url}, ${config.userName}, ${config.password})
        """)
    }

    public void modify(DatabaseConfiguration config) {
        this.db.update("""
            UPDATE DATABASE_CONFIGURATION
               SET NAME=${config.name},
                   DRIVER=${config.driver},
                   URL=${config.url},
                   USER_NAME=${config.userName},
                   PASSWORD=${config.password}
             WHERE ID=${config.id}
        """)
    }

    public void remove(long id) {
        this.db.delete("DELETE FROM DATABASE_CONFIGURATION WHERE ID=${id}")
    }
}
