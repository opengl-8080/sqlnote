package sqlnote.rest.query

import groovy.json.JsonBuilder

class SeeOtherResponseBuilder {
    
    static String build(int cnt, String requestUrl, String queryString) {
        def json = new JsonBuilder()
        
        json {
            recordCount cnt
            url "${requestUrl}?${queryString}&type=csv"
        }
        
        return json.toString()
    }
}
