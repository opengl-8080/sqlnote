package sqlnote.db

import groovy.sql.Sql

public class DatabaseAccess {
    private static Sql sql;
    
    public synchronized static void init(url) {
        if (!sql) {
            sql = Sql.newInstance(url, 'SA', '', 'org.hsqldb.jdbc.JDBCDriver');
        }
    }
    
    public synchronized static void init() {
        if (!sql) {
            sql = Sql.newInstance('jdbc:hsqldb:file:db/sqlnote;shutdown=true', 'SA', '', 'org.hsqldb.jdbc.JDBCDriver');
        }
    }
    
    static void withTransaction(closure) {
        sql.withTransaction(closure)
    }
    
    static Long insertSingle(sqlText) {
        def generated = sql.executeInsert(sqlText)
        
        if (generated[0]) {
            generated[0][0]
        }
    }
    
    static void delete(sqlText) {
        sql.executeUpdate(sqlText)
    }
    
    static Object firstRow(sqlText, Closure closure) {
        closure(sql.firstRow(sqlText))
    }
    
    static List collect(sqlText, Closure mapper) {
        sql.rows(sqlText).collect(mapper)
    }
}
