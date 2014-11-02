package sqlnote.db

class HsqlDatabase extends ExternalDatabase {

    @Override
    public String resolveType(String type) {
        switch (type) {
            case 'INTEGER':
            case 'INT':
            case 'DOUBLE':
            case 'PRECISION':
            case 'DECIMAL':
            case 'NUMERIC':
            case 'SMALLINT':
            case 'BIGINT':
            case 'REAL':
                ColumnMetaData.TYPE_NUMBER
                break
                
            case 'VARCHAR':
            case 'VARCHAR_IGNORECASE':
            case 'CHAR':
            case 'CHARACTER':
            case 'LONGVARCHAR':
                ColumnMetaData.TYPE_STRING
                break
                
            case 'DATE':
            case 'TIME':
            case 'TIMESTAMP':
            case 'DATETIME':
                ColumnMetaData.TYPE_DATE
                break
            
            default:
                ColumnMetaData.TYPE_STRING
                break
        }
    }

}
