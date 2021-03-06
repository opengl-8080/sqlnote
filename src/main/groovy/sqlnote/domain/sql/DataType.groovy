package sqlnote.domain.sql

import java.sql.Date as SqlDate
import java.text.ParseException

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.DateUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sqlnote.domain.IllegalParameterException;

enum DataType {
    STRING {
        <T> T convert(String parameterName, String src) {
            src
        }
    },

    NUMBER{
        <T> T convert(String parameterName, String src) {
            if (StringUtils.isEmpty(src)) {
                this.throwException(parameterName);
            }
            
            try {
                return new BigDecimal(src)
            } catch (NumberFormatException e) {
                this.throwException(parameterName);
            }
        }
        
        private void throwException(name) {
            throw new IllegalParameterException("${name} は数値で指定してください。")
        }
    },

    DATE {
        <T> T convert(String parameterName, String src) {
            if (StringUtils.isEmpty(src)) {
                throw new IllegalParameterException("${parameterName} は日付で指定してください。")
            }
            
            try {
                return parsetToSqlDate(src)
            } catch (ParseException e) {
                logger.warn("failed to parse date text => ${src}, ${e.message}")
                throw new IllegalParameterException("${parameterName} は日付で指定してください。")
            }
        }
        
        private SqlDate parsetToSqlDate(String src) {
            Date date = DateUtils.parseDate(src, 'yyyy-MM-dd', 'yyyy/MM/dd', 'yyyy-MM-dd HH:mm:ss', 'yyyy/MM/dd HH:mm:ss')
            return new SqlDate(date.time)
        }
    };

    private static final Logger logger = LoggerFactory.getLogger(DataType.class);
    
    abstract <T> T convert(String parameterName, String src);
}
