package fr.rob.core;

import fr.rob.core.config.Config;
import fr.rob.core.config.ConfigLoaderInterface;
import fr.rob.core.config.commons.configuration2.ConfigLoader;
import fr.rob.core.initiator.Initiator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseApplication {

    private final List<AbstractModule> modules = new ArrayList<>();
    protected final Initiator initiator = new Initiator();
    protected final ConfigLoaderInterface configLoader = new ConfigLoader();
    protected String env;

    public BaseApplication(String env) {
        this.env = env;
    }

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

    public Config loadConfig(File file) {
        return configLoader.loadConfigFromFile(file);
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
