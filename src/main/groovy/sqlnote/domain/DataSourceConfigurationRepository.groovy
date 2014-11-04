package sqlnote.domain

interface DataSourceConfigurationRepository {
    
    List<DataSourceConfiguration> findAll();
    DataSourceConfiguration findById(long id);
    void register(DataSourceConfiguration config);
    void modify(DataSourceConfiguration config);
    void remove(long id);
}
