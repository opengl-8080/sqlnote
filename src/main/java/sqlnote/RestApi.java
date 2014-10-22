package sqlnote;

import static spark.Spark.*;
import static spark.SparkBase.*;
import static sqlnote.rest.UrlBuilder.*;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sqlnote.db.DatabaseAccess;
import sqlnote.rest.DeleteSql;
import sqlnote.rest.ErrorMessageBuilder;
import sqlnote.rest.GetAllSql;
import sqlnote.rest.GetSqlDetail;
import sqlnote.rest.PostSql;
import sqlnote.rest.PutSql;

public class RestApi {
    private static final Logger logger = LoggerFactory.getLogger(RestApi.class);
    
    public static void main(String[] args) {
        migrateDatabase();
        setPort(PORT);
        externalStaticFileLocation("src/main/webapp");
        DatabaseAccess.init();
        
        before(API_FILTER, (req, res) -> {
            res.type("application/json");
            logger.debug("{} : {}", req.requestMethod(), req.pathInfo());
        });
        
        get(GET_ALL_SQL_PATH, (req, res) -> {
            String response = new GetAllSql().execute(id -> buildSqlDetailUrl(id));
            res.status(200);
            return response;
        });
        
        post(POST_SQL_PATH, (req, res) -> {
            new PostSql().execute();
            res.status(201);
            return "";
        });
        
        delete(DELETE_SQL_PATH, (req, res) -> {
            new DeleteSql().execute(Long.parseLong(req.params("id")));
            res.status(204);
            return "";
        });
        
        get(GET_SQL_DETAIL, (req, res) -> {
            String response = new GetSqlDetail().execute(Long.parseLong(req.params("id")));
            res.status(200);
            return response;
        });
        
        put(PUT_SQL, (req, res) -> {
            new PutSql().execute(Long.parseLong(req.params("id")), req.body());
            res.status(200);
            return "";
        });
        
        after(API_FILTER, (req, res) -> {
            logger.info("{} : {} - Status : {}", req.requestMethod(), req.pathInfo(), res.raw().getStatus());
        });
        
        exception(SqlNotFoundException.class, (e, req, res) -> {
            res.status(404);
            res.body(ErrorMessageBuilder.build("SQL が存在しません。削除されている可能性があります。"));
            
            logger.warn("{} : {} - Status : {} - SQL({}) is not found.", req.requestMethod(), req.pathInfo(), res.raw().getStatus(), ((SqlNotFoundException)e).getId());
        });
        
        exception(IllegalParameterException.class, (e, req, res) -> {
            res.status(400);
            res.body(ErrorMessageBuilder.build(e.getMessage()));
            
            logger.warn("{} : {} - Status : {} - {}", req.requestMethod(), req.pathInfo(), res.raw().getStatus(), e.getMessage());
        });
    }

    private static void migrateDatabase() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(DatabaseAccess.URL, DatabaseAccess.USER, DatabaseAccess.PASS);
        flyway.setPlaceholderPrefix("#{");
//        flyway.clean();
        flyway.migrate();
        
        System.out.println("migrate test database");
    }
}
