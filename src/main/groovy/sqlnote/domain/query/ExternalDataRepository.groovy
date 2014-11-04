package sqlnote.domain.query


interface ExternalDataRepository {
    
    void export(long sqlId, long dsId, Map<String, String> condition, ResponseWriter rw);
}
