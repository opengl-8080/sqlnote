package sqlnote

class SqlNotFoundException extends Exception {
    
    long id
    
    def SqlNotFoundException(long id) {
        this.id = id
    }
}
