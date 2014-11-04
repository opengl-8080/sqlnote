package sqlnote.domain

interface ExternalDataRepository {
    
    void export(long sqlId, long dsId, Map<String, String> condition, ResponseWriter rw);
}
