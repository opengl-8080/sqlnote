package sqlnote;

import static spark.Spark.*;
import static spark.SparkBase.*;
import static sqlnote.rest.UrlBuilder.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.eclipse.jetty.io.RuntimeIOException;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import sqlnote.db.DataSourceCache;
import sqlnote.db.SystemDataSource;
import sqlnote.domain.DataSourceConfigurationRepository;
import sqlnote.domain.EntityNotFoundException;
import sqlnote.domain.IllegalParameterException;
import sqlnote.domain.ResponseWriter;
import sqlnote.domain.TooManyQueryDataException;
import sqlnote.domain.UnConnectableDatabaseException;
import sqlnote.rest.ErrorMessageBuilder;
import sqlnote.rest.dbconfig.DeleteDataSource;
import sqlnote.rest.dbconfig.GetAllDataSource;
import sqlnote.rest.dbconfig.GetDataSourceDetail;
import sqlnote.rest.dbconfig.PostDataSource;
import sqlnote.rest.dbconfig.PutDataSource;
import sqlnote.rest.dbconfig.VerifyDataSource;
import sqlnote.rest.query.CsvResponseWriter;
import sqlnote.rest.query.DefaultResponseWriter;
import sqlnote.rest.query.QueryData;
import sqlnote.rest.query.SeeOtherResponseBuilder;
import sqlnote.rest.sql.DeleteSql;
import sqlnote.rest.sql.GetAllSql;
import sqlnote.rest.sql.GetSqlDetail;
import sqlnote.rest.sql.PostSql;
import sqlnote.rest.sql.PutSql;




public class RestApi {
    private static final Logger logger = LoggerFactory.getLogger(RestApi.class);
    
    public static void main(String[] args) {
        migrateDatabase(SystemDataSource.init());
        loadDataSource();
        setPort(PORT);
        staticFileLocation("/webapp");
//        externalStaticFileLocation("src/main/resources/webapp");
        
        defineSqlApi();
        defineQueryApi();
        defineDatabaseConfigApi();

        defineFilter();
        defineExceptionHandler();
    }

    private static void migrateDatabase(DataSource ds) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(ds);
        flyway.setPlaceholderPrefix("#{");
//        flyway.clean();
        flyway.migrate();
        
        System.out.println("migrate test database");
    }
    
    private static void loadDataSource() {
        SystemDataSource.with(db -> {
            new DataSourceConfigurationRepository(db).findAll().forEach(config -> {
                DataSourceCache.put(config.getId(), config);
            });
        });
    }
    
    private static void defineFilter() {
        before(API_FILTER, (req, res) -> {
            res.type("application/json");
            logger.debug("{} : {}?{}", req.requestMethod(), req.pathInfo(), req.queryString());
        });
        
        after(API_FILTER, (req, res) -> {
            logger.info("{} : {} - Status : {}", req.requestMethod(), req.pathInfo(), res.raw().getStatus());
        });
    }
    
    private static void defineDatabaseConfigApi() {
        get(GET_ALL_DATASOURCE_PATH, (req, res) -> {
            String response = new GetAllDataSource().execute();
            res.status(200);
            return response;
        });
        get(GET_DATASOURCE_DETAIL_PATH, (req, res) -> {
            String response = new GetDataSourceDetail().execute(getId(req));
            res.status(200);
            return response;
        });
        
        post(POST_DATASOURCE_PATH, (req, res) -> {
            new PostDataSource().execute(req.body());
            res.status(201);
            return "";
        });
        
        delete(DELETE_DATASOURCE_PATH, (req, res) -> {
            new DeleteDataSource().execute(getId(req));
            res.status(204);
            return "";
        });
        
        put(PUT_DATASOURCE_PATH, (req, res) -> {
            new PutDataSource().execute(getId(req), req.body());
            res.status(200);
            return "";
        });
        
        get(VERIFY_DATASOURCE_PATH, (req, res) -> {
            new VerifyDataSource().execute(getId(req));
            res.status(200);
            return "";
        });
        
        get(DUMP_DATA_SOURCE_CACHE, (req, res) -> {
            DataSourceCache.dump();
            return "";
        });
    }
    
    private static long getId(Request req) {
        return Long.parseLong(req.params("id"));
    }

    private static void defineSqlApi() {
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
            new DeleteSql().execute(getId(req));
            res.status(204);
            return "";
        });
        
        get(GET_SQL_DETAIL, (req, res) -> {
            String response = new GetSqlDetail().execute(getId(req));
            res.status(200);
            return response;
        });
        
        put(PUT_SQL, (req, res) -> {
            new PutSql().execute(getId(req), req.body());
            res.status(200);
            return "";
        });
    }

    private static void defineQueryApi() {
        get(EXECUTE_SQL, (req, res) -> {
            long sqlId = Long.parseLong(req.params("id"));
            long dsId = Long.parseLong(req.queryParams("dataSource"));
            Map<String, String[]> queryMap = req.queryMap("s").toMap();
            ResponseWriter rw = createResponseWriter(req, res);
            
            if (isCsvRequest(req)) {
                setHeadersForCsvOutput(res);
            }
            
            try {
                new QueryData().execute(sqlId, dsId, queryMap, rw);
                res.status(200);
                return "";
            } catch (TooManyQueryDataException e) {
                res.type("application/json");
                res.status(303);
                return SeeOtherResponseBuilder.build(e.getRecordCount(), req.url(), req.queryString());
            }
        });
    }
    
    private static ResponseWriter createResponseWriter(Request req, Response res) {
        if (isCsvRequest(req)) {
            return new CsvResponseWriter(getOutputStream(res));
        } else {
            return new DefaultResponseWriter(getOutputStream(res));
        }
    }
    
    private static boolean isCsvRequest(Request req) {
        return "csv".equals(req.queryParams("type"));
    }
    
    private static OutputStream getOutputStream(Response res) {
        try {
            return res.raw().getOutputStream();
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }
    
    private static void setHeadersForCsvOutput(Response res) {
        res.type("text/csv");
        String s = DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss");
        res.header("content-disposition", "attachment; filename='query_result_" + s + ".txt'");
    }
    
    private static void defineExceptionHandler() {
        exception(EntityNotFoundException.class, (e, req, res) -> {
            res.status(404);
            res.body(ErrorMessageBuilder.build(e.getMessage()));
            
            logger.warn("{} : {} - Status : {} - SQL({}) is not found.", req.requestMethod(), req.pathInfo(), res.raw().getStatus(), ((EntityNotFoundException)e).getId());
        });
        
        exception(IllegalParameterException.class, (e, req, res) -> {
            handleError(400, e, req, res);
        });
        
        exception(UnConnectableDatabaseException.class, (e, req, res) -> {
            handleError(202, e, req, res);
        });
        
        exception(Exception.class, (e, req, res) -> {
            logger.error("unknow error", e);
            handleError(500, e, req, res);
        });
    }
    
    private static void handleError(int status, Throwable e, Request req, Response res) {
        res.status(status);
        res.body(ErrorMessageBuilder.build(e.getMessage()));
        
        logger.error("{} : {} - Status : {} - {}", req.requestMethod(), req.pathInfo(), res.raw().getStatus(), e.getMessage());
    }
}
