package sqlnote.domain

class IllegalParameterException extends RuntimeException {
    
    String message
    public IllegalParameterException(String message) {
        this.message = message;
    }
    
}
