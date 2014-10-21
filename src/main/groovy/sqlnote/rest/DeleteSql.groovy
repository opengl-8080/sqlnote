package sqlnote.rest

import com.sun.corba.se.spi.activation.Repository;

import sqlnote.db.SqlNoteRepository

class DeleteSql {
    
    void execute(long id) {
        SqlNoteRepository repository = new SqlNoteRepository()
        
        repository.findById(id) // if not found then throw exception
        repository.remove(id)
    }
}
