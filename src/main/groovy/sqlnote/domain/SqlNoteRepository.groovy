package sqlnote.domain

import sqlnote.db.DatabaseAccess;

class SqlNoteRepository {
    
    DatabaseAccess db
    
    public SqlNoteRepository(DatabaseAccess db) {
        this.db = db
    }
    
    public SqlNote findById(long id) {
        this.db.firstRow("SELECT * FROM SQL_NOTE WHERE ID=${id}") { row ->
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
        note.parameters = this.queryParameterNames(row.ID)
        
        note
    }
    
    private List<SqlParameter> queryParameterNames(long sqlId) {
        // テストケースではデータソースの設定が違う！
        this.db.collect("SELECT * FROM SQL_PARAMETERS WHERE SQL_ID=${sqlId} ORDER BY SORT_ORDER ASC") {
            new SqlParameter(it.NAME, DataType.valueOf(it.DATA_TYPE))
        }
    }

    public List<SqlNote> findAll() {
        this.db.collect("SELECT * FROM SQL_NOTE ORDER BY ID ASC") { row ->
            this.buildSqlNote(row)
        }
    }

    public void register(SqlNote note) {
        long id = this.db.insertSingle("INSERT INTO SQL_NOTE (TITLE, SQL_TEMPLATE) VALUES (${note.title}, ${note.sqlTemplate})")
        note.id = id
        
        insertSqlParameters(note.id, note.parameters);
    }

    public void remove(long id) {
        this.db.delete("DELETE FROM SQL_PARAMETERS WHERE SQL_ID=${id}")
        this.db.delete("DELETE FROM SQL_NOTE WHERE ID=${id}")
    }

    public void modify(SqlNote note) {
        note.verify()
        
        this.db.delete("DELETE FROM SQL_PARAMETERS WHERE SQL_ID=${note.id}")
        this.db.update("UPDATE SQL_NOTE SET TITLE=${note.title}, SQL_TEMPLATE=${note.sqlTemplate} WHERE ID=${note.id}")
        
        insertSqlParameters(note.id, note.parameters);
    }
    
    private void insertSqlParameters(long sqlId, List<SqlParameter> sqlParameters) {
        sqlParameters.eachWithIndex { param, i ->
            this.db.insertSingle("INSERT INTO SQL_PARAMETERS VALUES (${sqlId}, ${param.name}, ${param.dataType.toString()}, ${i+1})")
        }
    }
}
