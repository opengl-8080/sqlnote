package sqlnote.domain.sql

import org.apache.commons.lang3.builder.HashCodeBuilder

import sqlnote.domain.IllegalParameterException;

class SqlParameter {
    
    String name
    DataType dataType
    
    def SqlParameter(String name, DataType dataType) {
        if (!name) {
            throw new IllegalParameterException('パラメータ名は必ず指定してください。')
        }
        
        this.name = name
        this.dataType = dataType
    }
    
    @Override
    public boolean equals(o) {
        if (o == null || !(o instanceof SqlParameter)) return false
        
        o.name == this.name && o.dataType == this.dataType
    }
    
    @Override
    public int hashCode() {
        new HashCodeBuilder().append(this.name).append(this.dataType).toHashCode()
    }

    @Override
    public String toString() {
        return "SqlParameter [name=" + name + ", dataType=" + dataType + "]";
    }
}
