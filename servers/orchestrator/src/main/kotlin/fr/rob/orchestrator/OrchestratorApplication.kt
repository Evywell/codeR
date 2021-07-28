package fr.rob.orchestrator

import fr.rob.core.AbstractModule
import fr.rob.core.DB_CONFIG
import fr.rob.core.SingleServerApplication
import fr.rob.core.config.Config
import fr.rob.core.config.commons.configuration2.ConfigLoader
import fr.rob.core.database.ConnectionManager
import fr.rob.core.event.EventManager
import fr.rob.core.initiator.Initiator
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.network.Server
import fr.rob.core.process.ProcessManager
import fr.rob.orchestrator.agent.AgentManagerProcess
import fr.rob.orchestrator.config.DatabaseConfigHandler
import fr.rob.orchestrator.config.DatabaseConfigHandler.Companion.CONFIG_KEY_DATABASES
import fr.rob.orchestrator.network.netty.OrchestratorNettyServer
import fr.rob.orchestrator.security.SecurityModule
import fr.rob.shared.orchestrator.OrchestratorRepository
import fr.rob.shared.orchestrator.OrchestratorRepositoryInterface
import fr.rob.shared.orchestrator.config.OrchestratorConfigHandler

class OrchestratorApplication(private val loggerFactory: LoggerFactoryInterface, env: String) :
    SingleServerApplication(env, loggerFactory.create("orchestrator"), ConfigLoader(), EventManager()) {

    private val connectionManager = ConnectionManager(eventManager)
    private val processManager = ProcessManager()

    private lateinit var orchestratorRepository: OrchestratorRepositoryInterface

    override fun initDependencies() {
        super.initDependencies()

        processManager.registerProcess(AgentManagerProcess::class) {
            AgentManagerProcess()
        }

        // Trigger the database configuration handler
        config!!.retrieveConfig(CONFIG_KEY_DATABASES)

        orchestratorRepository = OrchestratorRepository(connectionManager.getConnection(DB_CONFIG)!!)
    }

    override fun afterRun() {
        server.start()
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(
            SecurityModule(
                config!!.retrieveConfig("orchestrator") as OrchestratorConfigHandler.OrchestratorConfig,
                orchestratorRepository,
                processManager
            )
        )
    }

    override fun registerInitiatorTasks(initiator: Initiator) {
        // Nothing to implement
    }

    override fun registerConfigHandlers(config: Config) {
        config
            .addHandler(DatabaseConfigHandler(connectionManager))
            .addHandler(OrchestratorConfigHandler())
    }

    override fun createServer(): Server = OrchestratorNettyServer(
        ORCHESTRATOR_SERVER_PORT,
        ORCHESTRATOR_SERVER_ENABLE_SSL,
        eventManager,
        null,
        loggerFactory,
        processManager
    )
}
