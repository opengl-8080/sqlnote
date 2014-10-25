package sqlnote.domain

import groovy.json.JsonBuilder
import sqlnote.db.ColumnMetaData

class ResponseWriter {
    
    BufferedWriter bw
    private int dataRowCount = 0
    
    def ResponseWriter(OutputStream os) {
        this.bw = new BufferedWriter(new OutputStreamWriter(os))
    }
    
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
    
    void writeColumnMetaDatas(List<ColumnMetaData> columnMetaDatas) {
        def json = new JsonBuilder()
        json(columnMetaDatas)
        
        this.bw.print(json.toString())
        this.bw.print(',"data":[')
    }
    
    void appendDataRow(map) {
        if (this.dataRowCount != 0) {
            this.bw.println(',')
        }
        
        def json = new JsonBuilder()
        json(map)
        
        this.bw.print(json.toString())
        
        this.dataRowCount++
    }
    
    private void end() {
        this.bw.println(']}')
    }
}
