package sqlnote;

import java.util.List;
import java.util.regex.Matcher
import java.util.regex.Pattern

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
}
