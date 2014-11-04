package sqlnote.db

import java.sql.ResultSet

import org.codehaus.groovy.runtime.GStringImpl

import sqlnote.domain.ExternalDataRepository;
import sqlnote.domain.ResponseWriter;
import sqlnote.domain.SqlNote;
import sqlnote.domain.SqlNoteRepository;
import sqlnote.domain.TemplateAnalyzer;

class ExternalDataRepositoryImpl implements ExternalDataRepository {
    
    @Override
    void export(long sqlId, long dsId, Map<String, String> condition, ResponseWriter rw) {
        GString sqlGString = this.buildSqlGString(sqlId, condition)
        
        ExternalDataSource.with(dsId) { DatabaseAccess db ->
            db.query(sqlGString) { ResultSet rs ->
                List<ColumnMetaData> metaDatas = ColumnMetaData.makeColumnMetaData(rs)
                
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
