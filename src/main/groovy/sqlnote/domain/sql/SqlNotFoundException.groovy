package sqlnote.domain.sql

import sqlnote.domain.EntityNotFoundException;

class SqlNotFoundException extends EntityNotFoundException {
    
    def SqlNotFoundException(long id) {
        super(id, "SQL が存在しません。削除されている可能性があります。")
    }
}
