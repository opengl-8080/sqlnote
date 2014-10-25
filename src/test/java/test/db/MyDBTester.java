package test.db;
import jp.classmethod.testing.database.DbUnitTester;
import jp.classmethod.testing.database.JdbcDatabaseConnectionManager;
import jp.classmethod.testing.database.YamlDataSet;

import org.dbunit.dataset.IDataSet;

import sqlnote.db.SystemDataSource;

public class MyDBTester extends DbUnitTester {
    
    private final Class<?> testClass;
    
    public MyDBTester() {
        this(new SqlNoteJdbcDatabaseConnectionManager(), null);
    }

    public MyDBTester(Class<?> testClass) {
        this(new SqlNoteJdbcDatabaseConnectionManager(), testClass);
    }
    
    private MyDBTester(JdbcDatabaseConnectionManager manager, Class<?> testClass) {
        super(manager);
        this.testClass = testClass;
        SystemDataSource.init("jdbc:hsqldb:file:testdb/sqlnote;shutdown=true");
    }

    private static class SqlNoteJdbcDatabaseConnectionManager extends JdbcDatabaseConnectionManager {
        SqlNoteJdbcDatabaseConnectionManager() {
            super("org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:testdb/sqlnote;shutdown=true");
            super.username = "SA";
            super.password = "";
        }
    }
    
    /**
     * 指定したリソース（yaml ファイル）を {@link IDataSet} で読み込みます。
     * 
     * @param resource 読み込みリソース
     * @return 読み込んだデータ
     */
    public IDataSet loadDataSet(String resource) {
        return YamlDataSet.load(this.testClass.getResourceAsStream(resource));
    }
}