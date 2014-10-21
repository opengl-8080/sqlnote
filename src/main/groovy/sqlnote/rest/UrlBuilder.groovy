package sqlnote.rest

class UrlBuilder {
    
    public static final int PORT = 48123
    
    public static final String URL_BASE = "http://localhost:${PORT}";
    public static final String API_PATH = "/sqlnote/api";
    public static final String API_FILTER = "${API_PATH}/*"
    public static final String GET_ALL_SQL_PATH = "${API_PATH}/sql";
    public static final String POST_SQL_PATH = GET_ALL_SQL_PATH;
    public static final String DELETE_SQL_PATH = "${GET_ALL_SQL_PATH}/:id";
    public static final String GET_SQL_DETAIL = DELETE_SQL_PATH;
    public static final String PUT_SQL = DELETE_SQL_PATH;

    public static String buildSqlDetailUrl(long id) {
        "${URL_BASE}${GET_ALL_SQL_PATH}/${id}"
    }

    public static String buildExecuteSqlUrl(long id) {
        "${buildSqlDetailUrl(id)}/result"
    }
    
}
