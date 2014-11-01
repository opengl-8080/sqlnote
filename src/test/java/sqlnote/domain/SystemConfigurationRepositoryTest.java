package sqlnote.domain;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import jp.classmethod.testing.database.Fixture;

import org.dbunit.dataset.IDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import test.db.MyDBTester;

@Fixture(resources="SystemConfigurationRepositoryTest.yaml")
public class SystemConfigurationRepositoryTest {

    @Rule
    public MyDBTester dbTester = new MyDBTester(SystemConfigurationRepositoryTest.class);
    
    private SystemConfigurationRepository repostitory;
    
    @Before
    public void setup() {
        repostitory = new SystemConfigurationRepository(dbTester.getDatabaseAccess());
    }
    
    @Test
    public void 検索() throws Exception {
        // exercise
        SystemConfiguration config = repostitory.find();
        
        // verify
        assertThat(config.getMaxRowNum(), is(500L));
    }
    
    @Test
    public void 更新() throws Exception {
        // setup
        SystemConfiguration config = new SystemConfiguration();
        config.setMaxRowNum(250L);
        
        // exercise
        repostitory.modify(config);
        
        // verify
        IDataSet expected = dbTester.loadDataSet("SystemConfigurationRepositoryTest_更新_expected.yaml");
        dbTester.verifyTable("SYSTEM_CONFIGURATION", expected);
    }
}
