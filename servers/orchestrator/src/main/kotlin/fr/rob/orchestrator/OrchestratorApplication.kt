package fr.rob.orchestrator

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.config.Config
import fr.rob.core.database.ConnectionManager
import fr.rob.core.event.EventManager
import fr.rob.core.initiator.Initiator
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.network.Server
import fr.rob.core.process.ProcessManager
import fr.rob.core.security.SecurityBanProcess
import fr.rob.orchestrator.config.DatabaseConfigHandler
import fr.rob.orchestrator.config.DatabaseConfigHandler.Companion.CONFIG_KEY_DATABASES
import fr.rob.orchestrator.network.netty.OrchestratorNettyServer

class OrchestratorApplication(private val loggerFactory: LoggerFactoryInterface, env: String) :
    BaseApplication(env, loggerFactory.create("orchestrator")) {

    private val eventManager = EventManager()
    private val connectionManager = ConnectionManager(eventManager)
    private val processManager = ProcessManager()

    override fun initDependencies() {
        super.initDependencies()

        // Trigger the database configuration handler
        config!!.retrieveConfig(CONFIG_KEY_DATABASES)
    }

    override fun run() {
        super.run()

        server?.start()
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        // Nothing to implement
    }

    override fun registerInitiatorTasks(initiator: Initiator) {
        // Nothing to implement
    }

    override fun registerConfigHandlers(config: Config) {
        config
            .addHandler(DatabaseConfigHandler(connectionManager))
    }

    override fun createServer(): Server = OrchestratorNettyServer(
            ORCHESTRATOR_SERVER_PORT,
            ORCHESTRATOR_SERVER_ENABLE_SSL,
            eventManager,
            processManager.getOrMakeProcess(SecurityBanProcess::class),
            loggerFactory,
            processManager
        )
}
