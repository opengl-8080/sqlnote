package sqlnote.domain;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import sqlnote.domain.TemplateAnalyzer;

public class TemplateAnalyzerTest {
    
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
}
