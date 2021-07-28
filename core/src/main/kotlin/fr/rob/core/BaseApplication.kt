package fr.rob.core

import fr.rob.core.config.Config
import fr.rob.core.config.ConfigLoaderInterface
import fr.rob.core.event.EventManagerInterface
import fr.rob.core.exception.ExceptionManager
import fr.rob.core.initiator.Initiator
import fr.rob.core.log.LoggerInterface
import java.io.File
import java.util.ArrayList

abstract class BaseApplication(
    open val env: String,
    logger: LoggerInterface,
    private val configLoader: ConfigLoaderInterface,
    val eventManager: EventManagerInterface
) {

    private val modules: MutableList<AbstractModule> = ArrayList()
    var config: Config? = null

    protected val initiator = Initiator()

    protected val exceptionManager = ExceptionManager(logger)

    protected abstract fun registerModules(modules: MutableList<AbstractModule>)

    protected abstract fun registerInitiatorTasks(initiator: Initiator)

    protected abstract fun registerConfigHandlers(config: Config)

    protected open fun initDependencies() {
        // Config
        config?.let { registerConfigHandlers(it) }
    }

    fun run() {
        exceptionManager.catchExceptions()
        initDependencies()

        // Modules
        registerModules(modules)

        for (module in modules) {
            module.boot()
        }

        // Loading tasks
        registerInitiatorTasks(initiator)

        afterRun()
    }

    open fun afterRun() {
        // Nothing to implement here (to be override in subclasses)
    }

    open fun shutdown() {
        // Nothing to implement here (to be override in subclasses)
    }

    open fun loadConfig(file: File): Config {
        config = configLoader.loadConfigFromFile(file)

        return config as Config
    }
}
