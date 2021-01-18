package fr.rob.core;

import fr.rob.core.config.Config;
import fr.rob.core.initiator.Initiator;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseApplication {

    private final List<AbstractModule> modules = new ArrayList<>();
    private final Map<String, Config> configs = new HashMap<>();
    protected final Initiator initiator = new Initiator();
    protected String env;

    protected abstract void registerModules(List<AbstractModule> modules);

    protected abstract void registerInitiatorTasks(Initiator initiator);

    public void run() {
        // Modules
        registerModules(modules);

        for (AbstractModule module : modules) {
            module.boot();
        }

        // Loading tasks
        registerInitiatorTasks(initiator);
    }

    public Config addConfigPath(String configName, URL configPath) {
        Config config = new Config(configPath);
        configs.put(configName, config);

        return config;
    }

    public Config getConfig(String configName) {
        return configs.getOrDefault(configName, null);
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
