package sqlnote.domain;import sqlnote.db.ColumnMetaData




public interface ResponseWriter {
    
    boolean canWrite(int recordCount)
    void write(Closure closure)
    void writeColumnInfo(List<ColumnMetaData> metaDatas)
    void appendDataRow(rowData)
}
