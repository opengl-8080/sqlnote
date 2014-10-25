package sqlnote.domain

class UnConnectableDatabaseException extends RuntimeException {
    
    def UnConnectableDatabaseException(String message) {
        super(message)
    }
}
