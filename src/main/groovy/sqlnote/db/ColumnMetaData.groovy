package sqlnote.db

import static java.sql.Types.*;
import java.sql.ResultSet;
import java.util.List;

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
    
    static List<ColumnMetaData> makeColumnMetaData(ResultSet rs) {
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
    
    private static String resolveType(int type) {
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
                return TYPE_NUMBER
            case CHAR:
            case VARCHAR:
            case LONGVARCHAR:
            case CLOB:
            case NCHAR:
            case NVARCHAR:
            case LONGNVARCHAR:
            case NCLOB:
                return TYPE_STRING
            case DATE:
            case TIME:
            case TIMESTAMP:
                return TYPE_DATE
            default:
                return TYPE_STRING
        }
    }
}
