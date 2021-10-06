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
import fr.rob.orchestrator.config.DatabaseConfigHandler.Companion.DB_PLAYERS
import fr.rob.orchestrator.instances.CreateInstanceProcess
import fr.rob.orchestrator.instances.DefaultInstancesRepository
import fr.rob.orchestrator.instances.InstanceManager
import fr.rob.orchestrator.instances.InstancesRepository
import fr.rob.orchestrator.instances.request.InstanceRequestGenerator
import fr.rob.orchestrator.instances.request.RequestNewInstanceProcess
import fr.rob.orchestrator.network.netty.OrchestratorNettyServer
import fr.rob.orchestrator.nodes.GameNodeManager
import fr.rob.orchestrator.security.SecurityModule
import fr.rob.shared.orchestrator.Orchestrator
import fr.rob.shared.orchestrator.OrchestratorRepository
import fr.rob.shared.orchestrator.OrchestratorRepositoryInterface
import fr.rob.shared.orchestrator.config.OrchestratorConfigHandler

open class OrchestratorApplication(private val loggerFactory: LoggerFactoryInterface, env: String) :
    SingleServerApplication(env, loggerFactory.create("orchestrator app"), ConfigLoader(), EventManager()) {

    private val processManager = ProcessManager()

    private lateinit var orchestratorRepository: OrchestratorRepositoryInterface

    val connectionManager = ConnectionManager(eventManager)
    lateinit var orchestrator: Orchestrator

    override fun initDependencies() {
        super.initDependencies()

        processManager.registerProcess(AgentManagerProcess::class) {
            AgentManagerProcess()
        }

        val instancesRepository = InstancesRepository(connectionManager.getConnection(DB_PLAYERS))
        val defaultInstanceRepository = DefaultInstancesRepository(connectionManager.getConnection(DB_CONFIG))
        val createInstanceProcess = CreateInstanceProcess(instancesRepository)
        orchestratorRepository = OrchestratorRepository(connectionManager.getConnection(DB_CONFIG))

        val orchestratorConfig = config!!.retrieveConfig("orchestrator") as OrchestratorConfigHandler.OrchestratorConfig
        orchestrator = orchestratorRepository.getOrchestratorById(orchestratorConfig.id)!!

        processManager.registerProcess(Orchestrator::class) { orchestrator }
        processManager.registerProcess(GameNodeManager::class) { GameNodeManager() }
        processManager.registerProcess(CreateInstanceProcess::class) { createInstanceProcess }
        processManager.registerProcess(InstanceManager::class) {
            InstanceManager(orchestrator, createInstanceProcess, defaultInstanceRepository, instancesRepository)
        }
        processManager.registerProcess(RequestNewInstanceProcess::class) {
            RequestNewInstanceProcess(InstanceRequestGenerator())
        }
    }

    override fun afterRun() {
        server.start()
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(
            SecurityModule(
                processManager
            )
        )
    }

    override fun registerInitiatorTasks(initiator: Initiator) {
        // Nothing to implement
    }

    override fun registerConfigHandlers(config: Config) {
        config
            .addHandler(DatabaseConfigHandler(connectionManager), false)
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
