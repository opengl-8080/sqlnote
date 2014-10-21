package sqlnote.rest

class BadRequestException extends Exception {
    
    String message
    public BadRequestException(String message) {
        this.message = message;
    }
    
}
