package sqlnote.domain

class DataSourceConfiguration {
    Long id
    String name
    String driver
    String url
    String userName
    String password
    
    void setName(String name) {
        if (!name) {
            throw new IllegalParameterException('名前は必ず指定してください。')
        }
        this.name = name
    }
}
