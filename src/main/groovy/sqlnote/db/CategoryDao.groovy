package sqlnote.db

import groovy.sql.Sql
import sqlnote.Category

class CategoryDao {
    
    public long insert(Category category) {
        DatabaseAccess.execute { Sql sql ->
            category.id = sql.executeInsert("INSERT INTO CATEGORY_NODE (TITLE) VALUES (${category.title})")[0][0]
        }
        
        category.id
    }

    public void delete(int id) {
        DatabaseAccess.execute { Sql sql ->
            sql.execute("DELETE FROM CATEGORY_NODE WHERE ID=${id}")
        }
    }

    public Category findById(long id) {
        def result;
        
        DatabaseAccess.query { Sql sql ->
            result = sql.firstRow("SELECT * FROM CATEGORY_NODE WHERE ID=${id}");
        }
        
        Category c = new Category();
        
        c.id = result.ID
        c.title = result.TITLE
        
        return c;
    }
}
