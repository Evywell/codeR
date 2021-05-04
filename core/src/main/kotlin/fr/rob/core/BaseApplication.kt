package fr.rob.core

import fr.rob.core.config.Config
import fr.rob.core.config.ConfigLoaderInterface
import fr.rob.core.config.commons.configuration2.ConfigLoader
import fr.rob.core.initiator.Initiator
import java.io.File
import java.util.ArrayList

abstract class BaseApplication(open val env: String, private val configLoader: ConfigLoaderInterface = ConfigLoader()) {

    private val modules: MutableList<AbstractModule> = ArrayList()
    var config: Config? = null

    protected val initiator = Initiator()

    protected abstract fun registerModules(modules: MutableList<AbstractModule>)

    protected abstract fun registerInitiatorTasks(initiator: Initiator)

    protected abstract fun registerConfigHandlers(config: Config)

    open fun run() {
        // Config
        config?.let { registerConfigHandlers(it) }

        // Modules
        registerModules(modules)

        for (module in modules) {
            module.boot()
        }

        // Loading tasks
        registerInitiatorTasks(initiator)
    }

    open fun loadConfig(file: File): Config {
        config = configLoader.loadConfigFromFile(file)

        return config as Config
    }
}
