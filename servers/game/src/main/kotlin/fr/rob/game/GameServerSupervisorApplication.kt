package fr.rob.game

import fr.rob.core.AbstractModule
import fr.rob.core.MultiServerApplication
import fr.rob.core.config.Config
import fr.rob.core.config.commons.configuration2.ConfigLoader
import fr.rob.core.database.ConnectionManager
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.core.event.EventManager
import fr.rob.core.initiator.Initiator
import fr.rob.core.log.LoggerFactory
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.process.ProcessManager
import fr.rob.game.config.EnvConfigHandler
import fr.rob.game.config.database.DatabaseConfigHandler
import fr.rob.game.config.server.NodesConfig
import fr.rob.game.config.server.NodesConfigHandler
import fr.rob.game.config.server.ServerConfigHandler
import fr.rob.game.database.DatabaseModule
import fr.rob.game.network.GameNodeManager
import fr.rob.game.network.Server
import fr.rob.game.network.event.GameNodeStarted
import fr.rob.game.network.event.NewGameNodeForOrchestratorListener
import fr.rob.game.network.netty.NettyGameServerFactory
import fr.rob.game.security.OrchestratorModule
import fr.rob.game.tasks.TaskLoadServerConfig
import fr.rob.game.tasks.repository.LoadServerRepository
import fr.rob.game.tasks.repository.LoadServerRepositoryInterface
import fr.rob.orchestrator.agent.AbstractAgent
import fr.rob.orchestrator.agent.NodeAgent
import fr.rob.shared.orchestrator.OrchestratorRepository
import fr.rob.shared.orchestrator.OrchestratorRepositoryInterface
import fr.rob.shared.orchestrator.config.OrchestratorConfigHandler

class GameServerSupervisorApplication(
    env: String,
    private val loggerFactory: LoggerFactoryInterface
) : MultiServerApplication(env, loggerFactory.create("game"), ConfigLoader(), EventManager()) {

    private val connectionManager = ConnectionManager(eventManager)
    private val processManager = ProcessManager()
    var connectionPoolManager = ConnectionPoolManager(4, connectionManager)

    lateinit var servers: Array<Server>
    lateinit var agent: NodeAgent

    lateinit var orchestratorRepository: OrchestratorRepositoryInterface

    override fun initDependencies() {
        super.initDependencies()

        val dbConfigPool = connectionPoolManager.getPool(fr.rob.core.DB_CONFIG)!!

        orchestratorRepository = OrchestratorRepository(dbConfigPool.getNextConnection())
    }

    override fun afterRun() {
        initiator
            .runTask(TASK_LOAD_SERVER_CONFIG) // Store server info

        // Launch the orchestrator agent
        agent = processManager.getOrMakeProcess(NodeAgent::class)
        agent.authenticate()

        eventManager.addEventListener(
            GameNodeStarted.GAME_NODE_STARTED_EVENT,
            NewGameNodeForOrchestratorListener(this)
        )

        val nodeManager = GameNodeManager(
            config?.get("nodes")!! as NodesConfig,
            NettyGameServerFactory(this, processManager, eventManager)
        )
        nodeManager.buildNodes()
    }

    fun notifyOrchestratorForNewGameNode(name: String, port: Int) {
        agent.registerNewGameNode(name, port)
    }

    override fun registerInitiatorTasks(initiator: Initiator) {
        @Suppress("UNCHECKED_CAST")
        servers = config!!.retrieveConfig(SERVER) as Array<Server>

        // Trigger the handle() of the database config handler
        config!!.retrieveConfig(DATABASE)

        val loadServerRepository: LoadServerRepositoryInterface =
            LoadServerRepository(
                (connectionPoolManager.getPool(DB_CONFIG) ?: throw Exception("Cannot get connection $DB_CONFIG"))
                    .getNextConnection()
            )

        initiator
            .addTask(TASK_LOAD_SERVER_CONFIG, TaskLoadServerConfig(servers, loadServerRepository))
    }

    override fun registerConfigHandlers(config: Config) {
        config
            .addHandler(EnvConfigHandler())
            .addHandler(DatabaseConfigHandler(connectionPoolManager))
            .addHandler(ServerConfigHandler())
            .addHandler(NodesConfigHandler())
            .addHandler(OrchestratorConfigHandler())
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(DatabaseModule(eventManager))
        modules.add(OrchestratorModule(
            orchestratorRepository,
            config!!.retrieveConfig("orchestrator") as OrchestratorConfigHandler.OrchestratorConfig,
            processManager,
            loggerFactory
        ))
    }
}
