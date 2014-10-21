package sqlnote

class SqlNote {
    Long id
    String title = '新規SQL'
    String sqlTemplate = '-- SQL を入力してください'
    List<String> parameterNames
    
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

    public void setParameterNames(List<String> parameterNames) {
        def unique = parameterNames.unique(false)
        if (unique != parameterNames) {
            throw new IllegalParameterException('パラメータ名が重複しています。')
        }
        
        parameterNames.each {
            if (it =~ /[\$\{\}]/) {
                throw new IllegalParameterException('パラメータ名に $, {, } は使用できません。')
            }
        }
        
        this.parameterNames = parameterNames;
    }
}
