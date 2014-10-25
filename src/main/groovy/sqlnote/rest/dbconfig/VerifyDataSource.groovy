package sqlnote.rest.dbconfig

import sqlnote.db.DataSourceCache;

class VerifyDataSource {
    
    void execute(long id) {
        DataSourceCache.verifyConnectivity(id)
    }
}
