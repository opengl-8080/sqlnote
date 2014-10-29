package sqlnote.db

class OracleDatabase extends ExternalDatabase {

    @Override
    public String resolveType(String type) {
        switch (type) {
            case 'NUMBER':
            case 'BINARY_FLOAT':
            case 'BINARY_DOUBLE':
            
                ColumnMetaData.TYPE_NUMBER
                break
                
            case 'VARCHAR2':
            case 'NVARCHAR2':
            case 'CHAR':
            case 'NCHAR':
            case 'LONG':
            case 'CLOB':
                ColumnMetaData.TYPE_STRING
                break
                
            case 'DATE':
            case 'TIMESTAMP':
                ColumnMetaData.TYPE_DATE
                break
            
            default:
                ColumnMetaData.TYPE_STRING
                break
        }
    }

}
