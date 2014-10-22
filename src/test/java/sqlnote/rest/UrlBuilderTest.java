package sqlnote.rest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import static sqlnote.rest.UrlBuilder.*;
import org.junit.Test;

public class UrlBuilderTest {
    
    @Test
    public void urlBase() throws Exception {
        assertThat(UrlBuilder.URL_BASE, is("http://localhost:" + PORT));
    }
    
    @Test
    public void apiPath() throws Exception {
        assertThat(UrlBuilder.API_PATH, is("/sqlnote/api"));
    }
    
    @Test
    public void apiFilter() throws Exception {
        assertThat(UrlBuilder.API_FILTER, is(API_PATH + "/*"));
    }
    
    @Test
    public void getAllSql() throws Exception {
        assertThat(UrlBuilder.GET_ALL_SQL_PATH, is(API_PATH + "/sql"));
    }
    
    @Test
    public void buildGetSqlDetail() throws Exception {
        assertThat(UrlBuilder.buildSqlDetailUrl(10), is(URL_BASE + GET_ALL_SQL_PATH + "/10"));
    }
    
    @Test
    public void postSql() throws Exception {
        assertThat(UrlBuilder.POST_SQL_PATH, is(GET_ALL_SQL_PATH));
    }
    
    @Test
    public void deleteSql() throws Exception {
        assertThat(UrlBuilder.DELETE_SQL_PATH, is(GET_ALL_SQL_PATH + "/:id"));
    }
    
    @Test
    public void getSqlDetail() throws Exception {
        assertThat(UrlBuilder.GET_SQL_DETAIL, is(DELETE_SQL_PATH));
    }
    
    @Test
    public void putSql() throws Exception {
        assertThat(UrlBuilder.PUT_SQL, is(DELETE_SQL_PATH));
    }
    
    @Test
    public void executeSql() throws Exception {
        assertThat(UrlBuilder.EXECUTE_SQL, is(DELETE_SQL_PATH + "/result"));
    }
    
    @Test
    public void buildExecuteSqlUrl() throws Exception {
        assertThat(UrlBuilder.buildExecuteSqlUrl(21), is(UrlBuilder.buildSqlDetailUrl(21) + "/result"));
    }
}
