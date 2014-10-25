package sqlnote.domain;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TemplateAnalyzerTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void バインドパラメータ文字列の抽出() throws Exception {
        // setup
        TemplateAnalyzer analyzer = new TemplateAnalyzer();
        
        String template = "${p1} not #{not} $[not] ${{not} ${p2}} ${not$} ${n$ot} ${not{} ${パラメータ} ${p1}";
        
        // exercise
        analyzer.analyze(template);
        
        // verify
        List<String> parameterNames = analyzer.getParameterNames();
        assertThat(parameterNames, contains("p1", "p2", "パラメータ", "p1"));
    }
    
    @Test
    public void バインドパラメータ以外の文字列の抽出() throws Exception {
        // setup
        TemplateAnalyzer analyzer = new TemplateAnalyzer();
        
        String template = "a${p1}bc${p2}def${p3}";
        
        // exercise
        analyzer.analyze(template);
        
        // verify
        List<String> strings = analyzer.getStrings();
        assertThat(strings, contains("a", "bc", "def"));
    }
    
    @Test
    public void 定義されていないパラメータが存在する場合_例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("unknown は SQL で使用されていません。");
        
        TemplateAnalyzer analyzer = new TemplateAnalyzer();
        analyzer.analyze("${param1}, ${param2}");
        
        Map<String, String> parameter = new HashMap<>();
        parameter.put("param1", "value");
        parameter.put("unknown", "value");
        
        // exercise
        analyzer.verify(parameter);
    }
    
    @Test
    public void SQLで使用されているパラメータが存在しない_例外がスローされる() throws Exception {
        // setup
        exception.expect(IllegalParameterException.class);
        exception.expectMessage("unuse はパラメータで宣言されていません。");
        
        TemplateAnalyzer analyzer = new TemplateAnalyzer();
        analyzer.analyze("${param1}, ${unuse}");
        
        Map<String, String> parameter = new HashMap<>();
        parameter.put("param1", "value");
        
        // exercise
        analyzer.verify(parameter);
    }
}
