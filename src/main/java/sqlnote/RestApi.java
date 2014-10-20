package sqlnote;

import static spark.Spark.*;

public class RestApi {
    
    public static void main(String[] args) {
        
        setPort(48123);
        externalStaticFileLocation("src/main/webapp");
        
        get("/api/test", (req, res) -> {
            return "hello";
        });
    }
}
