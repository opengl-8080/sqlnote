package sqlnote

class SqlNote {
    Long id
    String title = '新規SQL'
    String sqlTemplate = '-- SQL を入力してください'
    List<SqlParameter> parameterNames
    
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

    public void setParameterNames(List<SqlParameter> parameters) {
        def unique = parameters.unique(false) {it.name}
        if (unique != parameters) {
            throw new IllegalParameterException('パラメータ名が重複しています。')
        }
        
        parameters.each {
            if (it.name =~ /[\$\{\}]/) {
                throw new IllegalParameterException('パラメータ名に $, {, } は使用できません。')
            }
        }
        
        this.parameterNames = parameters;
    }
}
