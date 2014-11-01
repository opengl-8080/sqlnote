package sqlnote.rest.query

import groovy.json.JsonBuilder
import sqlnote.db.ColumnMetaData
import sqlnote.db.SystemDataSource
import sqlnote.domain.ResponseWriter
import sqlnote.domain.SystemConfigurationRepository

class DefaultResponseWriter implements ResponseWriter {
    
    BufferedWriter bw
    private int dataRowCount = 0
    private long maxRowNum
    
    def DefaultResponseWriter(OutputStream os) {
        this.bw = new BufferedWriter(new OutputStreamWriter(os))
        this.maxRowNum = this.loadMaxRowNum()
    }
    
    private long loadMaxRowNum() {
        long maxRowNum
        
        SystemDataSource.with { db ->
            maxRowNum = new SystemConfigurationRepository(db).find().maxRowNum
        }
        
        return maxRowNum
    }
    
    @Override
    void write(Closure closure) {
        try {
            this.begin()
            closure()
            this.end()
        } finally {
            this.bw.close()
        }
    }
    
    private void begin() {
        this.bw.print('{"metaData":')
    }
    
    @Override
    void writeColumnInfo(List<ColumnMetaData> columnMetaDatas) {
        def json = new JsonBuilder()
        json(columnMetaDatas)
        
        this.bw.print(json.toString())
        this.bw.print(',"data":[')
    }
    
    @Override
    void appendDataRow(rowData) {
        if (this.dataRowCount != 0) {
            this.bw.println(',')
        }
        
        def json = new JsonBuilder()
        json(rowData)
        
        this.bw.print(json.toString())
        
        this.dataRowCount++
    }
    
    private void end() {
        this.bw.println(']}')
    }

    @Override
    public boolean canWrite() {
        return this.dataRowCount < this.maxRowNum
    }
}
