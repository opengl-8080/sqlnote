package sqlnote.domain

class TooManyQueryDataException extends RuntimeException {
    int recordCount
    
    def TooManyQueryDataException(int recordCount) {
        this.recordCount = recordCount
    }
}
