package test.db;

import org.flywaydb.core.Flyway;

public class DatabaseMigration {
    
    public static void main(String[] args) {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:hsqldb:file:testdb/sqlnote;shutdown=true", "SA", "");
        flyway.clean();
        flyway.migrate();
        
        System.out.println("migrate test database");
    }
}
