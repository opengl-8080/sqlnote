package sqlnote.db

import groovy.sql.Sql

import java.sql.ResultSet

public class DatabaseAccess {
    private static Sql sql;
    public static final String URL = 'jdbc:hsqldb:file:db/sqlnote;shutdown=true'
    public static final String USER = 'SA'
    public static final String PASS = ''
    
    public synchronized static void init(url) {
        if (!sql) {
            sql = Sql.newInstance(url, USER, PASS, 'org.hsqldb.jdbc.JDBCDriver');
        }
    }
    
    public synchronized static void init() {
        if (!sql) {
            sql = Sql.newInstance(URL, USER, PASS, 'org.hsqldb.jdbc.JDBCDriver');
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
    
    static void update(sqlText) {
        sql.executeUpdate(sqlText)
    }
    
    static void withResultSet(sqlText, closure) {
        closure(sql.executeQuery(sqlText))
    }
}
