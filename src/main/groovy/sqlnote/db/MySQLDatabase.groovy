package sqlnote.db

class MySQLDatabase extends ExternalDatabase {

    @Override
    public String resolveType(String type) {
        switch (type) {
            case 'TINYINT':
            case 'SMALLINT':
            case 'MEDIUMINT':
            case 'INT':
            case 'BIGINT':
            case 'FLOAT':
            case 'DOUBLE':
            
                ColumnMetaData.TYPE_NUMBER
                break
                
            case 'CHAR':
            case 'VARCHAR':
            case 'TINYTEXT':
            case 'TEXT':
            case 'MEDIUMTEXT':
            case 'LONGTEXT':
                ColumnMetaData.TYPE_STRING
                break
                
            case 'DATE':
            case 'DATETIME':
            case 'TIMESTAMP':
                ColumnMetaData.TYPE_DATE
                break
            
            default:
                ColumnMetaData.TYPE_STRING
                break
        }
    }

}
