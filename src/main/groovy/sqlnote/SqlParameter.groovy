package sqlnote

import org.apache.commons.lang3.builder.HashCodeBuilder

class SqlParameter {
    
    String name
    DataType dataType
    
    def SqlParameter(String name, DataType dataType) {
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
