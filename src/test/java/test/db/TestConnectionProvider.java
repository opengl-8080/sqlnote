package test.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.rules.ExternalResource;

import sqlnote.db.DatabaseAccess;
import sqlnote.db.SystemDataSource;

public class TestConnectionProvider extends ExternalResource {

    private Connection con;
    
    @Override
    public void before() {
        SystemDataSource.init("jdbc:hsqldb:file:testdb/sqlnote;shutdown=true");
        this.con = SystemDataSource.getConnection();
    }
    
    @Override
    public void after() {
        try {
            this.con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public DatabaseAccess getDatabaseAccess() {
        return new DatabaseAccess(this.con);
    }
}
