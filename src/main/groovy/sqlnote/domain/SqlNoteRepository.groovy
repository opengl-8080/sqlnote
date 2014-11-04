package sqlnote.domain

interface SqlNoteRepository {
    
    SqlNote findById(long id);
    List<SqlNote> findAll();
    void register(SqlNote note);
    void remove(long id);
    void modify(SqlNote note);
}
