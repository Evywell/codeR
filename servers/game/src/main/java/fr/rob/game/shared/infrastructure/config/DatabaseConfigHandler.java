package fr.rob.game.shared.infrastructure.config;

import fr.rob.core.config.ConfigHandlerInterface;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import java.util.Iterator;

public class DatabaseConfigHandler implements ConfigHandlerInterface {
    @Override
    public String getRootName() {
        return "databases";
    }

    @Override
    public String getName() {
        return "database";
    }

    @Override
    public Object handle(ObjectNode node) {
        if (!node.has("databases")) {
            return null;
        }

        JsonNode databasesNode = node.get("databases");
        Iterator<String> databasesNodeFieldNames = databasesNode.getFieldNames();

        DatabaseConfig databaseConfig = new DatabaseConfig();

        while (databasesNodeFieldNames.hasNext()) {
            String databaseName = databasesNodeFieldNames.next();
            JsonNode databaseNode = databasesNode.findValue(databaseName);
            databaseConfig.addDatabase(databaseName, databaseNode);
        }

        return databaseConfig;
    }

}
