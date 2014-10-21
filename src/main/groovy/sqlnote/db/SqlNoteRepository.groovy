package sqlnote.db

import sqlnote.SqlNotFoundException
import sqlnote.SqlNote

class SqlNoteRepository {

    public SqlNote findById(long id) {
        DatabaseAccess.firstRow("SELECT * FROM SQL_NOTE WHERE ID=${id}") { row ->
            if (row) {
                this.buildSqlNote(row)
            } else {
                throw new SqlNotFoundException(id)
            }
        }
    }
    
    private SqlNote buildSqlNote(row) {
        SqlNote note = new SqlNote()
        
        note.id = row.ID
        note.title = row.TITLE
        note.sqlTemplate = row.SQL_TEMPLATE
        note.parameterNames = this.queryParameterNames(row.ID)
        
        note
    }
    
    private List<String> queryParameterNames(long sqlId) {
        DatabaseAccess.collect("SELECT * FROM SQL_PARAMETERS WHERE SQL_ID=${sqlId} ORDER BY SORT_ORDER ASC") { it.NAME }
    }

    public List<SqlNote> findAll() {
        DatabaseAccess.collect("SELECT * FROM SQL_NOTE ORDER BY ID ASC") { row ->
            this.buildSqlNote(row)
        }
    }

    public void register(SqlNote note) {
        DatabaseAccess.withTransaction {
            long id = DatabaseAccess.insertSingle("INSERT INTO SQL_NOTE (TITLE, SQL_TEMPLATE) VALUES (${note.title}, ${note.sqlTemplate})")
            note.id = id
            
            note.parameterNames.eachWithIndex { name, i ->
                DatabaseAccess.insertSingle("INSERT INTO SQL_PARAMETERS VALUES (${id}, ${name}, ${i+1})")
            }
        }
    }

    public void remove(long id) {
        DatabaseAccess.withTransaction {
            DatabaseAccess.delete("DELETE FROM SQL_PARAMETERS WHERE SQL_ID=${id}")
            DatabaseAccess.delete("DELETE FROM SQL_NOTE WHERE ID=${id}")
        }
    }

    public void modify(SqlNote note) {
        DatabaseAccess.withTransaction {
            DatabaseAccess.delete("DELETE FROM SQL_PARAMETERS WHERE SQL_ID=${note.id}")
            DatabaseAccess.update("UPDATE SQL_NOTE SET TITLE=${note.title}, SQL_TEMPLATE=${note.sqlTemplate} WHERE ID=${note.id}")
            
            println note.parameterNames
            
            note.parameterNames.eachWithIndex { name, i ->
                DatabaseAccess.insertSingle("INSERT INTO SQL_PARAMETERS VALUES (${note.id}, ${name}, ${i+1})")
            }
        }
    }
    
}
