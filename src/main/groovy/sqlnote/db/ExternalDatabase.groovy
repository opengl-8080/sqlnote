package sqlnote.db

import java.sql.ResultSet

abstract class ExternalDatabase {
    
    List<ColumnMetaData> makeColumnMetaData(ResultSet rs) {
        def metaData = rs.metaData
        int cnt = metaData.columnCount
        
        List list = []
        
        for (int i=1; i<=cnt; i++) {
            def label = metaData.getColumnLabel(i)
            def type = this.resolveType(metaData.getColumnTypeName(i))
            
            list << new ColumnMetaData(name: label, type: type)
        }
        
        return list
    }
    
    abstract String resolveType(String type);
}
