package sqlnote.db;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import jp.classmethod.testing.database.Fixture;

import org.dbunit.dataset.IDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import sqlnote.Category;
import test.db.MyDBTester;

public class CategoryDaoTest {
    
    @Rule
    public MyDBTester db = new MyDBTester(CategoryDaoTest.class);
    
    private CategoryDao dao;
    
    @Before
    public void setup() {
        dao = new CategoryDao();
    }
    
    @Test
    @Fixture(resources="CategoryDaoTest_検索.yaml")
    public void 検索() throws Exception {
        // exercise
        Category category = dao.findById(2L);
        
        // verify
        assertThat(category.getTitle(), is("テストカテゴリ"));
    }
    
    @Test
    @Fixture(resources="CategoryDaoTest_新規追加.yaml")
    public void 新規追加() throws Exception {
        // setup
        Category category = new Category();
        category.setTitle("テストカテゴリ");
        
        // exercise
        long generatedId = dao.insert(category);
        
        // verify
        IDataSet expected = db.loadDataSet("CategoryDaoTest_新規追加_expected.yaml");
        db.verifyTable("CATEGORY_NODE", expected, "ID");
        
        Category stored = dao.findById(generatedId);
        assertThat(category, is(samePropertyValuesAs(stored)));
    }
    
    @Test
    @Fixture(resources="CategoryDaoTest_削除.yaml")
    public void 削除() throws Exception {
        // exercise
        dao.delete(2);
        
        // verify
        IDataSet expected = db.loadDataSet("CategoryDaoTest_削除_expected.yaml");
        db.verifyTable("CATEGORY_NODE", expected);
    }
}
