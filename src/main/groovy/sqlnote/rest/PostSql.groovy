package sqlnote.rest

import sqlnote.SqlNote;
import sqlnote.db.SqlNoteRepository;

class PostSql {
    
    void execute() {
        SqlNote note = new SqlNote()
        new SqlNoteRepository().register(note)
    }
}
