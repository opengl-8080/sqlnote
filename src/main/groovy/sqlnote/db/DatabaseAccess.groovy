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
    
    static void execute(closure) {
        closure(sql)
    }
    
    static void query(closure) {
        closure(sql)
    }
}
