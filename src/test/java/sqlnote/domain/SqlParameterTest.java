package sqlnote.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sqlnote.domain.sql.DataType;
import sqlnote.domain.sql.SqlParameter;

public class SqlParameterTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void 名前が空文字の場合は例外がスローされる() {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("パラメータ名は必ず指定してください。");
        
        // exercise
        new SqlParameter("", DataType.STRING);
    }
    
    @Test
    public void 名前がnullの場合は例外がスローされる() {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("パラメータ名は必ず指定してください。");
        
        // exercise
        new SqlParameter(null, DataType.STRING);
    }
}
