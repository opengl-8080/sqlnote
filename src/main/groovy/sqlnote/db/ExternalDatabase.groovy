package sqlnote.db

import java.sql.ResultSet

import static java.sql.Types.*;
import sqlnote.domain.DataType;

class ExternalDatabase {
    
    List<ColumnMetaData> makeColumnMetaData(ResultSet rs) {
        def metaData = rs.metaData
        int cnt = metaData.columnCount
        
        List list = []
        
        for (int i=1; i<=cnt; i++) {
            def label = metaData.getColumnLabel(i)
            def type = this.resolveType(metaData.getColumnType(i))
            
            list << new ColumnMetaData(name: label, type: type)
        }
        
        return list
    }
    
    private String resolveType(int type) {
        switch (type) {
            case TINYINT:
            case SMALLINT:
            case INTEGER:
            case BIGINT:
            case FLOAT:
            case REAL:
            case DOUBLE:
            case NUMERIC:
            case DECIMAL:
                return ColumnMetaData.TYPE_NUMBER
            case CHAR:
            case VARCHAR:
            case LONGVARCHAR:
            case CLOB:
            case NCHAR:
            case NVARCHAR:
            case LONGNVARCHAR:
            case NCLOB:
                return ColumnMetaData.TYPE_STRING
            case DATE:
            case TIME:
            case TIMESTAMP:
                return ColumnMetaData.TYPE_DATE
            default:
                return ColumnMetaData.TYPE_STRING
        }
    }
}
