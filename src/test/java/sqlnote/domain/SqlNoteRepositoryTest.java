package sqlnote.domain;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static test.db.TestHelper.*;

import java.util.Arrays;
import java.util.List;

import jp.classmethod.testing.database.Fixture;

import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import sqlnote.db.SqlNoteRepositoryImpl;
import test.db.MyDBTester;
import test.db.TestConnectionProvider;

@Fixture(resources="SqlNoteRepositoryTest.yaml")
public class SqlNoteRepositoryTest {
    
    @Rule
    public MyDBTester dbTester = new MyDBTester(SqlNoteRepositoryTest.class);
    @Rule
    public TestConnectionProvider con = new TestConnectionProvider();
    
    private SqlNoteRepositoryImpl repository;
    
    @Before
    public void setup() throws Exception {
        repository = new SqlNoteRepositoryImpl(con.getDatabaseAccess());
    }
    
    @Test
    public void 検索() throws Exception {
        // exercise
        SqlNote actual = repository.findById(2L);
        
        // verify
        assertThat(actual.getId(), is(2L));
        assertThat(actual.getTitle(), is("title2"));
        assertThat(actual.getSqlTemplate(), is("sql2"));
        assertThat(actual.getParameters(), is(contains(dateParameter("param1"), stringParameter("param2"))));
    }
    
    @Test(expected=SqlNotFoundException.class)
    public void 検索_該当なしの場合は例外をスロー() throws Exception {
        // exercise
        repository.findById(99L);
    }
    
    @Test
    public void 全件検索() throws Exception {
        // exercise
        List<SqlNote> actual = repository.findAll();
        
        // verify
        assertThat(actual.size(), is(4));
        
        assertThat(actual.get(0).getId(), is(1L));
        assertThat(actual.get(0).getTitle(), is("title1"));
        assertThat(actual.get(0).getSqlTemplate(), is("sql1"));
        assertThat(actual.get(0).getParameters(), is(empty()));

        assertThat(actual.get(1).getId(), is(2L));
        assertThat(actual.get(1).getTitle(), is("title2"));
        assertThat(actual.get(1).getSqlTemplate(), is("sql2"));
        assertThat(actual.get(1).getParameters(), is(contains(dateParameter("param1"), stringParameter("param2"))));

        assertThat(actual.get(2).getId(), is(3L));
        assertThat(actual.get(2).getTitle(), is("title3"));
        assertThat(actual.get(2).getSqlTemplate(), is("sql3"));
        assertThat(actual.get(2).getParameters(), is(contains(numberParameter("param3"))));

        assertThat(actual.get(3).getId(), is(90L));
        assertThat(actual.get(3).getTitle(), is("title90"));
        assertThat(actual.get(3).getSqlTemplate(), is("sql90"));
        assertThat(actual.get(3).getParameters(), is(empty()));
    }
    
    @Test
    public void 追加() throws Exception {
        // setup
        SqlNote note = new SqlNote();
        note.setTitle("新規追加");
        note.setSqlTemplate("SQL Template");
        note.setParameters(Arrays.asList(stringParameter("aaa"), numberParameter("bbb")));
        
        // exercise
        repository.register(note);
        
        // verify
        IDataSet expected = dbTester.loadDataSet("SqlNoteRepositoryTest_追加_expected.yaml");
        dbTester.verifyTable("SQL_NOTE", expected, "ID");
        dbTester.verifyTable("SQL_PARAMETERS", expected, "SQL_ID");
        
        assertThat(note.getId(), is(greaterThan(0L)));
    }
    
    @Test
    public void 削除() throws Exception {
        // exercise
        repository.remove(2L);
        
        // verify
        IDataSet expected = dbTester.loadDataSet("SqlNoteRepositoryTest_削除_expected.yaml");
        dbTester.verifyTable("SQL_NOTE", expected);
        dbTester.verifyTable("SQL_PARAMETERS", expected);
    }
    
    @Test
    public void 更新() throws Exception {
        // setup
        SqlNote note = repository.findById(2L);
        
        note.setTitle("タイトル更新");
        note.setSqlTemplate("SQL更新 ${param1_update} ${param2_update} ${param3_update}");
        note.setParameters(Arrays.asList(stringParameter("param1_update"), dateParameter("param2_update"), numberParameter("param3_update")));
        
        // exercise
        repository.modify(note);
        
        // verify
        IDataSet expected = dbTester.loadDataSet("SqlNoteRepositoryTest_更新_expected.yaml");
        dbTester.verifyTable("SQL_NOTE", expected);
        
        ITable actualSqlParameters = dbTester.getConnection().createQueryTable("SQL_PARAMETERS", "SELECT * FROM SQL_PARAMETERS ORDER BY SQL_ID ASC, SORT_ORDER ASC");
        ITable expectedSqlParameters = expected.getTable("SQL_PARAMETERS");
        
        Assertion.assertEquals(expectedSqlParameters, actualSqlParameters);
    }
}
