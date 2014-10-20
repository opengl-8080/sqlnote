package sqlnote.db;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import jp.classmethod.testing.database.Fixture;

import org.dbunit.dataset.IDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import sqlnote.SqlNote;
import test.db.MyDBTester;

@Fixture(resources="SqlNoteRepositoryTest.yaml")
public class SqlNoteRepositoryTest {
    
    @Rule
    public MyDBTester dbTester = new MyDBTester(SqlNoteRepositoryTest.class);
    
    private SqlNoteRepository repository;
    
    @Before
    public void setup() {
        repository = new SqlNoteRepository();
    }
    
    @Test
    public void 検索() throws Exception {
        // exercise
        SqlNote actual = repository.findById(2L);
        
        // verify
        assertThat(actual.getId(), is(2L));
        assertThat(actual.getTitle(), is("title2"));
        assertThat(actual.getSqlTemplate(), is("sql2"));
        assertThat(actual.getParameterNames(), is(contains("param1", "param2")));
    }
    
    @Test
    public void 全件検索() throws Exception {
        // exercise
        List<SqlNote> actual = repository.findAll();
        
        // verify
        assertThat(actual.get(0).getId(), is(1L));
        assertThat(actual.get(0).getTitle(), is("title1"));
        assertThat(actual.get(0).getSqlTemplate(), is("sql1"));
        assertThat(actual.get(0).getParameterNames(), is(empty()));

        assertThat(actual.get(1).getId(), is(2L));
        assertThat(actual.get(1).getTitle(), is("title2"));
        assertThat(actual.get(1).getSqlTemplate(), is("sql2"));
        assertThat(actual.get(1).getParameterNames(), is(contains("param1", "param2")));
    }
    
    @Test
    public void 追加() throws Exception {
        // setup
        SqlNote note = new SqlNote();
        note.setTitle("新規追加");
        note.setSqlTemplate("SQL Template");
        note.setParameterNames(Arrays.asList("aaa", "bbb"));
        
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
}
