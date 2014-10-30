package sqlnote.db

import groovy.sql.Sql

import java.sql.Connection
import java.sql.ResultSet;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DatabaseAccess {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseAccess.class)
    Sql sql
    
    public DatabaseAccess(Connection connection) {
        this.sql = new Sql(connection);
        this.sql.resultSetType = ResultSet.TYPE_SCROLL_INSENSITIVE
    }
    
    Long insertSingle(sqlText) {
        def generated = this.sql.executeInsert(sqlText)
        
        if (generated[0]) {
            generated[0][0]
        }
    }
    
    void delete(sqlText) {
        this.sql.executeUpdate(sqlText)
    }
    
    Object firstRow(sqlText, Closure closure) {
        closure(this.sql.firstRow(sqlText))
    }
    
    List collect(sqlText, Closure mapper) {
        this.sql.rows(sqlText).collect(mapper)
    }
    
    void update(sqlText) {
        this.sql.executeUpdate(sqlText)
    }
    
    void withResultSet(sqlText, closure) {
        closure(this.sql.executeQuery(sqlText))
    }
    
    void query(sqlText, closure) {
        this.sql.query(sqlText, closure)
    }
}
