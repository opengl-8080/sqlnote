package sqlnote.rest.system

import java.text.NumberFormat

import org.apache.commons.lang3.StringUtils;

import sqlnote.domain.IllegalParameterException
import sqlnote.domain.system.SystemConfiguration;

class SystemConfigParser {
    
    SystemConfiguration parse(param) {
        this.verify(param)
        
        SystemConfiguration config = new SystemConfiguration()
        config.maxRowNum = param.maxRowNum as long
        
        return config
    }
    
    private void verify(param) {
        def s = param.maxRowNum as String
        
        if (StringUtils.isEmpty(s)) {
            throw new IllegalParameterException('最大出力件数は必ず指定してください。')
        }
        
        try {
            if (Long.parseLong(s) < 1) {
                throw new IllegalParameterException('最大出力件数は 1 以上の値を指定してください。')
            }
        } catch (NumberFormatException e) {
            throw new IllegalParameterException('最大出力件数は数値で指定してください。')
        }
    }
}
