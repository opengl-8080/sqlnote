package sqlnote.domain

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap

import javax.sql.DataSource

import org.apache.commons.dbcp2.BasicDataSource

import sqlnote.db.DatabaseAccess;
import sqlnote.db.SystemDataSource;

class DataSourceRepository {
    
    public static final DataSourceRepository instance = new DataSourceRepository()
    
    private Map<Integer, DataSource> dataSourceCache = new ConcurrentHashMap<>()
    
    boolean has(Integer key) {
        this.dataSourceCache.containsKey(key)
    }
    
    synchronized void putOnMemory(Integer key, DataSource ds) {
        this.dataSourceCache.put(key, ds)
    }
    
    synchronized void put(Integer key, BasicDataSource ds) {
        // TODO
        throw new UnsupportedOperationException('not implemented')
    }
    
    DataSource get(Integer key) {
        this.dataSourceCache.get(key)
    }
    
    synchronized void load() {
        // TODO データソースの定義を DB から取得して DataSource を作成し、キャッシュに保存する
        throw new UnsupportedOperationException('not implemented')
    }
    
    synchronized void remove(Integer key) {
        // TODO
        throw new UnsupportedOperationException('not implemented')
    }
}
