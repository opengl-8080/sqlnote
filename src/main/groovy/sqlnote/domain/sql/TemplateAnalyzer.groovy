package sqlnote.domain.sql;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher
import java.util.regex.Pattern

import sqlnote.domain.IllegalParameterException;

class TemplateAnalyzer {
    private static final Pattern PARAMETER_PATTERN = ~/\$\{[^\$\{]+?\}/
    private List<String> parameterNames
    private List<String> strings;
    
    public void analyze(String template) {
        this.parameterNames = PARAMETER_PATTERN.matcher(template).collect { it.replaceAll(/[\$\{\}]/, '') }
        this.strings = template.split(PARAMETER_PATTERN.pattern) as List
    }

    public List<String> getParameterNames() {
        new ArrayList<>(this.parameterNames)
    }

    public List<String> getStrings() {
        new ArrayList<>(this.strings)
    }

    public void verify(Map<String, String> parameter) {
        parameter.each { key, value ->
            if (!this.parameterNames.contains(key)) {
                throw new IllegalParameterException("${key} は SQL で使用されていません。")
            }
        }
        
        this.parameterNames.each { name ->
            if (!parameter.containsKey(name)) {
                throw new IllegalParameterException("${name} はパラメータで宣言されていません。")
            }
        }
    }
}
