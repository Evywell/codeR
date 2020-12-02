package fr.rob.core.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private URL configPath;
    private ObjectNode node;
    private boolean initialized = false;
    private Map<String, ConfigHandlerInterface> configHandlers = new HashMap<>();

    public Config(URL configPath) {
        this.configPath = configPath;
    }

    public Config handler(ConfigHandlerInterface configHandler) {
        configHandlers.put(configHandler.getName(), configHandler);

        return this;
    }

    public Object get(String name) {
        initializeIfNotAlready();

        ConfigHandlerInterface configHandler = configHandlers.get(name);

        if (configHandler.getRootName() == null) {
            return null;
        }

        return configHandler.handle(node);
    }

    public void initializeIfNotAlready() {
        if (initialized) {
            return;
        }

        try {
            node = new ObjectMapper().readValue(configPath, ObjectNode.class);
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
