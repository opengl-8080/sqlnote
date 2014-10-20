package sqlnote.db;

import jp.classmethod.testing.database.Fixture;

import org.dbunit.dataset.IDataSet;
import org.junit.Rule;
import org.junit.Test;

import sqlnote.Category;
import test.db.MyDBTester;

public class NodeDaoTest {

    @Rule
    public MyDBTester db = new MyDBTester(NodeDaoTest.class);
    
    @Test
    @Fixture(resources="NodeDaoTest_カテゴリ追加.yaml")
    public void カテゴリ追加() throws Exception {
        // setup
        NodeDao dao = new NodeDao();
        
        Category category = new Category();
        category.setTitle("テストカテゴリ");
        
        // exercise
        dao.createNewCategory(category, 2);
        
        // verify
        IDataSet expected = db.loadDataSet("NodeDaoTest_カテゴリ追加_expected.yaml");
        db.verifyTable("NODE", expected, "ID");
        db.verifyTable("CATEGORY_NODE", expected, "ID");
    }
    
}
