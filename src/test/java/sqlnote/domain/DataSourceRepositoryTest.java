package sqlnote.domain;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.sql.Connection;
import java.util.List;

import jp.classmethod.testing.database.Fixture;

import org.dbunit.dataset.IDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import sqlnote.db.DatabaseAccess;
import sqlnote.db.SystemDataSource;
import test.db.MyDBTester;

@Fixture(resources="DataSourceRepositoryTest.yaml")
public class DataSourceRepositoryTest {

    @Rule
    public MyDBTester dbTester = new MyDBTester(DataSourceRepositoryTest.class);
    
    private DatabaseConfigurationRepository repository;
    
    @Before
    public void setup() {
        Connection con = SystemDataSource.getConnection();
        DatabaseAccess db = new DatabaseAccess(con);
        repository = new DatabaseConfigurationRepository(db);
    }
    
    @Test
    public void 全件検索() throws Exception {
        // exercise
        List<DatabaseConfiguration> configurations = repository.findAll();
        
        // verify
        for (long i=1; i<=3; i++) {
            DatabaseConfiguration config = configurations.get((int)i - 1);
            
            assertThat(config.getId(), is(i));
            assertThat(config.getName(), is("db" + i));
            assertThat(config.getDriver(), is("driver" + i));
            assertThat(config.getUrl(), is("url" + i));
            assertThat(config.getUserName(), is("username" + i));
            assertThat(config.getPassword(), is("password" + i));
        }
    }
    
    @Test
    public void 一件検索() throws Exception {
        // exercise
        DatabaseConfiguration config = repository.findById(2L);
        
        // verify
        assertThat(config.getId(), is(2L));
        assertThat(config.getName(), is("db2"));
        assertThat(config.getDriver(), is("driver2"));
        assertThat(config.getUrl(), is("url2"));
        assertThat(config.getUserName(), is("username2"));
        assertThat(config.getPassword(), is("password2"));
    }
    
    @Test
    public void 追加() throws Exception {
        // setup
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setName("db4");
        config.setDriver("driver4");
        config.setUrl("url4");
        config.setUserName("username4");
        config.setPassword("password4");
        
        // exercise
        repository.register(config);
        
        // verify
        IDataSet expected = dbTester.loadDataSet("DataSourceRepositoryTest_追加_expected.yaml");
        dbTester.verifyTable("DATABASE_CONFIGURATION", expected, "ID");
        
        assertThat(config.getId(), is(greaterThan(0L)));
    }
    
    @Test
    public void 更新() throws Exception {
        // setup
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setId(2L);
        config.setName("DB");
        config.setDriver("DRIVER");
        config.setUrl("URL");
        config.setUserName("USERNAME");
        config.setPassword("PASSWORD");
        
        // exercise
        repository.modify(config);
        
        // verify
        IDataSet expected = dbTester.loadDataSet("DataSourceRepositoryTest_更新_expected.yaml");
        dbTester.verifyTable("DATABASE_CONFIGURATION", expected);
    }
    
    @Test
    public void 削除() throws Exception {
        // exercise
        repository.remove(2L);
        
        // verify
        IDataSet expected = dbTester.loadDataSet("DataSourceRepositoryTest_削除_expected.yaml");
        dbTester.verifyTable("DATABASE_CONFIGURATION", expected);
    }
}
