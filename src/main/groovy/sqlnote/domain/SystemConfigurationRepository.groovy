package sqlnote.domain

import sqlnote.db.DatabaseAccess;

class SystemConfigurationRepository {

    DatabaseAccess db
    
    public SystemConfigurationRepository(DatabaseAccess databaseAccess) {
        this.db = databaseAccess
    }

    public SystemConfiguration find() {
        return db.firstRow("SELECT * FROM SYSTEM_CONFIGURATION") { row ->
            def config = new SystemConfiguration()
            config.maxRowNum = row.MAX_ROW_NUM
            
            return config
        }
    }

    public void modify(SystemConfiguration config) {
        this.db.update("UPDATE SYSTEM_CONFIGURATION SET MAX_ROW_NUM=${config.maxRowNum} WHERE ID=1")
    }

}
