package sqlnote.rest.system;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sqlnote.domain.IllegalParameterException;
import sqlnote.domain.SystemConfiguration;

public class SystemConfigParserTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none()
    
    SystemConfigParser parser = new SystemConfigParser()
    
    @Test
    void 最大出力件数が存在しない場合_例外がスローされる() {
        // setup
        exception.expect(IllegalParameterException.class)
        exception.expectMessage('最大出力件数は必ず指定してください。')
        
        def param = [:]
        
        // exercise
        parser.parse(param)
    }
    
    @Test
    void 最大出力件数が空文字の場合_例外がスローされる() {
        // setup
        exception.expect(IllegalParameterException.class)
        exception.expectMessage('最大出力件数は必ず指定してください。')
        
        def param = [maxRowNum: '']
        
        // exercise
        parser.parse(param)
    }
    
    @Test
    void 最大出力件数が0の場合_例外がスローされる() {
        // setup
        exception.expect(IllegalParameterException.class)
        exception.expectMessage('最大出力件数は 1 以上の値を指定してください。')
        
        def param = [maxRowNum: 0]
        
        // exercise
        parser.parse(param)
    }
    
    @Test
    void 最大出力件数がマイナス１の場合_例外がスローされる() {
        // setup
        exception.expect(IllegalParameterException.class)
        exception.expectMessage('最大出力件数は 1 以上の値を指定してください。')
        
        def param = [maxRowNum: -1]
        
        // exercise
        parser.parse(param)
    }
    
    @Test
    void 最大出力件数が数値以外の場合_例外がスローされる() {
        // setup
        exception.expect(IllegalParameterException.class)
        exception.expectMessage('最大出力件数は数値で指定してください。')
        
        def param = [maxRowNum: 'aaa']
        
        // exercise
        parser.parse(param)
    }
    
    @Test
    void エラーがなければSystemConfigurationに変換できる() {
        // setup
        def param = [maxRowNum: 543]
        
        // exercise
        SystemConfiguration config = parser.parse(param)
        
        // verify
        assertThat(config.maxRowNum, is(543L))
    }
}
