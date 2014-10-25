package sqlnote.rest.query

import groovy.lang.Closure;

import java.io.OutputStream;
import java.util.List;

import sqlnote.db.ColumnMetaData;
import sqlnote.domain.ResponseWriter;

class CsvResponseWriter implements ResponseWriter {

    private BufferedWriter bw
    private List<ColumnMetaData> metaDatas
    
    public CsvResponseWriter(OutputStream out) {
        this.bw = new BufferedWriter(new OutputStreamWriter(out))
    }

    @Override
    public boolean canWrite(int recordCount) {
        return recordCount < Long.MAX_VALUE
    }

    @Override
    public void write(Closure closure) {
        try {
            closure()
        } finally {
            this.bw.close()
        }
    }

    @Override
    public void writeColumnInfo(List<ColumnMetaData> metaDatas) {
        bw.println metaDatas.collect {it.name}.join('\t')
        this.metaDatas = metaDatas
    }

    @Override
    public void appendDataRow(rowData) {
        bw.println this.metaDatas.collect { rowData[it.name] }.join("\t")
    }
}
