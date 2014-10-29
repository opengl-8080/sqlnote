package sqlnote.db

class UnSupportedDatabaseException extends Exception {
    
    def UnSupportedDatabaseException(databaseName) {
        super("${databaseName} はサポートされていません。")
    }
}
