package sqlnote.db

import sqlnote.domain.EntityNotFoundException;

class DataSourceCacheNotFoundException extends EntityNotFoundException {
    
    def DataSourceCacheNotFoundException(long id) {
        super(id, "データソースが存在しません。削除されている可能性があります。")
    }
}
