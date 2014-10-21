package sqlnote.rest

import groovy.json.JsonBuilder

class ErrorMessageBuilder {
    
    public static String build(String msg) {
        def json = new JsonBuilder()
        
        json {
            message msg
        }
        
        json.toString()
    }
}
