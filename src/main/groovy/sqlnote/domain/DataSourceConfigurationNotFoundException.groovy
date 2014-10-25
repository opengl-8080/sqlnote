package sqlnote.domain

class DataSourceConfigurationNotFoundException extends EntityNotFoundException {
    
    def DataSourceConfigurationNotFoundException(long id) {
        super(id, "データソース定義が存在しません。削除されている可能性があります。")
    }
}
