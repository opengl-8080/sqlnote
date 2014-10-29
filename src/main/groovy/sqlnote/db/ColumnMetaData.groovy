package sqlnote.db

class ColumnMetaData {
    
    public static final String TYPE_NUMBER = 'number'
    public static final String TYPE_STRING = 'string'
    public static final String TYPE_DATE = 'date'
    
    String name
    String type
    
    def format(value) {
        if (this.type == TYPE_DATE) {
            Date d = value as Date
            return d?.format("yyyy-MM-dd'T'HH:mm:ssZ")
        } else {
            return value
        }
    }
}
