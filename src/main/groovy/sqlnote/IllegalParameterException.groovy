package sqlnote

class IllegalParameterException extends Exception {
    
    String message
    public IllegalParameterException(String message) {
        this.message = message;
    }
    
}
