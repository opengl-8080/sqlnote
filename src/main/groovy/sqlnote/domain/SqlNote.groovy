package sqlnote.domain

class SqlNote {
    Long id
    String title = '新規SQL'
    String sqlTemplate = '-- SQL を入力してください'
    List<SqlParameter> parameters
    
    void setTitle(title) {
        if (!title) {
            throw new IllegalParameterException('タイトルは必ず指定してください。')
        }
        this.title = title
    }

    void setSqlTemplate(String sqlTemplate) {
        if (!sqlTemplate) {
            throw new IllegalParameterException('SQL は必ず指定してください。')
        }
        this.sqlTemplate = sqlTemplate;
    }

    public void setParameters(List<SqlParameter> parameters) {
        def unique = parameters.unique(false) {it.name}
        if (unique != parameters) {
            throw new IllegalParameterException('パラメータ名が重複しています。')
        }
        
        parameters.each {
            if (it.name =~ /[\$\{\}]/) {
                throw new IllegalParameterException('パラメータ名に $, {, } は使用できません。')
            }
        }
        
        this.parameters = parameters;
    }

    public void verify() {
        TemplateAnalyzer analyzer = new TemplateAnalyzer()
        analyzer.analyze(this.sqlTemplate)
        
        def bindParameters = analyzer.parameterNames
        def parameterNames = this.parameters.collect {it.name}
        
        anyOf(bindParameters).notExistsIn(parameterNames) {
            throw new IllegalParameterException("${it} はパラメータに定義されていません。")
        }
        
        anyOf(parameterNames).notExistsIn(bindParameters) {
            throw new IllegalParameterException("${it} は SQL で使用されていません。")
        }
    }
    
    private static AnyOf anyOf(List list) {
        new AnyOf(list: list)
    }
    
    private static class AnyOf {
        List list
        
        void notExistsIn(List otherList, closure) {
            this.list.each {
                if (!otherList.contains(it)) {
                    closure(it)
                }
            }
        }
    }
}