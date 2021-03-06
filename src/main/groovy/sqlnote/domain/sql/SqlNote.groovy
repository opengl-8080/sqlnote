package sqlnote.domain.sql

import java.sql.Date as SqlDate
import java.text.ParseException

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.domain.IllegalParameterException;

class SqlNote implements Cloneable {
    private static final Logger logger = LoggerFactory.getLogger(SqlNote.class)
    
    Long id
    String title = 'New SQL'
    String sqlTemplate = '-- Enter a SQL'
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

    public <T> T convert(String parameterName, String value) {
        // パラメータの数がパフォーマンスに影響を与えるほど増えることは考えにくいので、この実装（線形探索）で様子見
        SqlParameter parameter = this.parameters.find {it.name == parameterName}
        parameter.dataType.convert(parameterName, value)
    }
    
    private SqlDate parsetToSqlDate(String text) {
        Date date = DateUtils.parseDate(src, 'yyyy-MM-dd', 'yyyy/MM/dd', 'yyyy-MM-dd HH:mm:ss', 'yyyy/MM/dd HH:mm:ss')
        return new SqlDate(date.time)
    }

    @Override
    public String toString() {
        return "SqlNote [id=" + id + ", title=" + title + ", sqlTemplate=" + sqlTemplate + ", parameters=" + parameters + "]";
    }

    public SqlNote copy() {
        SqlNote cp = super.clone()
        
        cp.id = null
        cp.title += ' - Copy'
        cp.parameters = new ArrayList(cp.parameters)
        
        return cp
    }
}
