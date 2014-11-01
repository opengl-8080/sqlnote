package sqlnote.domain

import java.sql.ResultSet

import org.codehaus.groovy.runtime.GStringImpl

import sqlnote.db.ColumnMetaData
import sqlnote.db.DatabaseAccess
import sqlnote.db.ExternalDataSource
import sqlnote.db.ExternalDatabase
import sqlnote.db.SystemDataSource
import sqlnote.rest.query.DefaultResponseWriter;

class ExternalDataRepository {
    
    void export(long sqlId, long dsId, Map<String, String> condition, ResponseWriter rw) {
        GString sqlGString = this.buildSqlGString(sqlId, condition)
        
        ExternalDataSource.with(dsId) { ExternalDatabase ex, DatabaseAccess db ->
            db.query(sqlGString) { ResultSet rs ->
                List<ColumnMetaData> metaDatas = ex.makeColumnMetaData(rs)
                
                rw.write {
                    rw.writeColumnInfo(metaDatas)
                    
                    while (rs.next() && rw.canWrite()) {
                        def data = this.convertToMap(metaDatas, rs)
                        rw.appendDataRow(data)
                    }
                }
            }
        }
    }
    
    private GString buildSqlGString(long sqlId, Map<String, String> condition) {
        SqlNote sql = this.findSqlNote(sqlId)
        
        TemplateAnalyzer analyzer = new TemplateAnalyzer()
        analyzer.analyze(sql.sqlTemplate)
        analyzer.verify(condition)
        
        def values = analyzer.parameterNames.collect { sql.convert(it, condition.get(it)) } as Object[]
        def strings = analyzer.strings as String[]
        
        return new GStringImpl(values, strings)
    }
    
    private SqlNote findSqlNote(long sqlId) {
        SqlNote sql
        
        SystemDataSource.with { db ->
            sql = new SqlNoteRepository(db).findById(sqlId)
        }
        
        return sql
    }
    
    private def convertToMap(List<ColumnMetaData> metaDatas, ResultSet rs) {
        def data = [:]
        
        metaDatas.eachWithIndex { col, i ->
            data[col.name] = col.format(rs.getObject(i + 1))
        }
        
        return data
    }
}
