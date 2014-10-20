package sqlnote;

import static spark.Spark.*;

import org.flywaydb.core.Flyway;

import sqlnote.db.DatabaseAccess;
import sqlnote.rest.GetAllSql;

public class RestApi {
    
    private static final int PORT = 48123;
    private static final String BASE_PATH = "/sqlnote";
    private static final String API_PATH = BASE_PATH + "/api";
    private static final String API_BASE_URL = "http://localhost:" + PORT + API_PATH;
    
    public static void main(String[] args) {
        migrateDatabase();
        
        setPort(PORT);
        externalStaticFileLocation("src/main/webapp");
        DatabaseAccess.init();
        
        before(API_PATH + "/*", (req, res) -> {
            res.type("application/json");
        });
        
        get(API_PATH + "/sql", (req, res) -> {
            return new GetAllSql().execute(id -> API_BASE_URL + "/sql/" + id);
        });
    }

    private static void migrateDatabase() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(DatabaseAccess.URL, DatabaseAccess.USER, DatabaseAccess.PASS);
        flyway.setPlaceholderPrefix("#{");
        flyway.clean();
        flyway.migrate();
        
        System.out.println("migrate test database");
    }
}
