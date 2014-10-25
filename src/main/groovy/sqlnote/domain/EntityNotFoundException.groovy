package sqlnote.domain

class EntityNotFoundException extends Exception {
    
    long id
    
    def EntityNotFoundException(long id, String message) {
        super(message)
        this.id = id
    }
}
