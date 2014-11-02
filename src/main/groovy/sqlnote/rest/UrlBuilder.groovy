package sqlnote.rest

import static sqlnote.rest.UrlBuilder.*;

import org.hsqldb.persist.LobManager.GET_LOB_PART;

class UrlBuilder {
    
    public static final int PORT = 48123
    
    public static final String URL_BASE = "http://localhost:${PORT}";
    public static final String API_PATH = "/sqlnote/api";
    public static final String API_FILTER = "${API_PATH}/*"
    
    // sql
    public static final String GET_ALL_SQL_PATH = "${API_PATH}/sql";
    public static final String POST_SQL_PATH = GET_ALL_SQL_PATH;
    public static final String DELETE_SQL_PATH = "${GET_ALL_SQL_PATH}/:id";
    public static final String GET_SQL_DETAIL = DELETE_SQL_PATH;
    public static final String PUT_SQL = DELETE_SQL_PATH;
    public static final String EXECUTE_SQL = "${DELETE_SQL_PATH}/result";
    public static final String COPY_SQL = GET_SQL_DETAIL;
    
    // datasource
    public static final String GET_ALL_DATASOURCE_PATH = "${API_PATH}/dataSource";
    public static final String POST_DATASOURCE_PATH = GET_ALL_DATASOURCE_PATH;
    public static final String DELETE_DATASOURCE_PATH = GET_ALL_DATASOURCE_PATH + "/:id";
    public static final String GET_DATASOURCE_DETAIL_PATH = DELETE_DATASOURCE_PATH;
    public static final String PUT_DATASOURCE_PATH = DELETE_DATASOURCE_PATH;
    public static final String VERIFY_DATASOURCE_PATH = DELETE_DATASOURCE_PATH + "/verify";
    public static final String DUMP_DATA_SOURCE_CACHE = GET_ALL_DATASOURCE_PATH + "/cache";
    
    // system configuration
    public static final String GET_SYSTEM_CONFIGURATION = "${API_PATH}/config";
    public static final String PUT_SYSTEM_CONFIGURATION = GET_SYSTEM_CONFIGURATION;
    
    public static String buildSqlDetailUrl(long id) {
        "${URL_BASE}${GET_ALL_SQL_PATH}/${id}"
    }

    public static String buildExecuteSqlUrl(long id) {
        "${buildSqlDetailUrl(id)}/result"
    }

    public static String buildVerifyDataSourceUrl(long id) {
        "${GET_ALL_DATASOURCE_PATH}/${id}/verify"
    }
    
}
