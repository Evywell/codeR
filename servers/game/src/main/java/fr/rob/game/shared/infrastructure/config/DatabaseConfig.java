package fr.rob.game.shared.infrastructure.config;

import org.codehaus.jackson.JsonNode;

public class DatabaseConfig {

    public Database jwtAuth;

    public void addDatabase(String databaseName, JsonNode databaseNode) {
        Database database = new Database();
        database.host = databaseNode.get("host").getTextValue();
        database.port = databaseNode.get("port").getLongValue();
        database.user = databaseNode.get("user").getTextValue();
        database.password = databaseNode.get("password").getTextValue();
        database.database = databaseNode.get("database").getTextValue();

        switch (databaseName) {
            case "jwtAuth":
                jwtAuth = database;
                break;
        }

    }

    public static class Database {

        public String host;
        public Long port;
        public String user;
        public String password;
        public String database;

    }
}
