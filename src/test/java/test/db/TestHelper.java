package test.db;

import sqlnote.domain.DataType;
import sqlnote.domain.SqlParameter;

public class TestHelper {
    
    public static SqlParameter stringParameter(String name) {
        return new SqlParameter(name, DataType.STRING);
    }

    public static SqlParameter numberParameter(String name) {
        return new SqlParameter(name, DataType.NUMBER);
    }

    public static SqlParameter dateParameter(String name) {
        return new SqlParameter(name, DataType.DATE);
    }
}
