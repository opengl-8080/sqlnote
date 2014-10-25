package sqlnote.rest.query

import groovy.json.JsonBuilder

class SeeOtherResponseBuilder {
    
    static String build(int cnt, String baseUrl) {
        def json = new JsonBuilder()
        
        json {
            recordCount cnt
            url "${baseUrl}&type=csv"
        }
        
        return json.toString()
    }
}
