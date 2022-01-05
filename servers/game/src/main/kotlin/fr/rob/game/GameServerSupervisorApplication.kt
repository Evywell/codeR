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
import fr.rob.core.network.v2.netty.client.NettyClient
import fr.rob.core.process.ProcessManager
import fr.rob.game.config.EnvConfigHandler
import fr.rob.game.config.database.DatabaseConfigHandler
import fr.rob.game.config.server.NodesConfig
import fr.rob.game.config.server.NodesConfigHandler
import fr.rob.game.config.server.ServerConfigHandler
import fr.rob.game.database.DatabaseModule
import fr.rob.game.network.Server
import fr.rob.game.network.node.GameNodeManager
import fr.rob.orchestrator.agent.NodeAgent
import fr.rob.orchestrator.agent.network.NodeAgentClient

class GameServerSupervisorApplication(
    env: String,
    private val loggerFactory: LoggerFactoryInterface
) : MultiServerApplication(env, loggerFactory.create("game"), ConfigLoader(), EventManager()) {

    private val connectionManager = ConnectionManager(eventManager)
    private val processManager = ProcessManager()
    var connectionPoolManager = ConnectionPoolManager(4, connectionManager)

    lateinit var servers: Array<Server>
    lateinit var agent: NodeAgent

    override fun afterRun() {
        /*
        initiator
            .runTask(TASK_LOAD_SERVER_CONFIG) // Store server info
        */
        val nodesConfig = config?.retrieveConfig("nodes")

        val nodeManager = GameNodeManager(
            nodesConfig!! as NodesConfig,
            loggerFactory
        )

        val adapter = NodeAgentAdapter(nodeManager)

        // Launch the orchestrator agent
        val agentLogger = LoggerFactory.create("agent")
        val client = NodeAgentClient(adapter, agentLogger)
        val process = NettyClient("orchestrator", 12345, client)

        agent = NodeAgent(client, process)
        agent.authenticate("azert")

        val nodes = nodeManager.buildNodes()

        for (node in nodes) {
            println("Register node ${node.info.name}")
            agent.registerNewGameNode(node.info.name, node.info.port)
        }
    }

    override fun registerInitiatorTasks(initiator: Initiator) {
        @Suppress("UNCHECKED_CAST")
        servers = config!!.retrieveConfig(SERVER) as Array<Server>

        // Trigger the handle() of the database config handler
        config!!.retrieveConfig(DATABASE)

        /*
        val loadServerRepository: LoadServerRepositoryInterface =
            LoadServerRepository(
                (connectionPoolManager.getPool(DB_CONFIG) ?: throw Exception("Cannot get connection $DB_CONFIG"))
                    .getNextConnection()
            )

        initiator
            .addTask(TASK_LOAD_SERVER_CONFIG, TaskLoadServerConfig(servers, loadServerRepository))
         */
    }

    override fun registerConfigHandlers(config: Config) {
        config
            .addHandler(EnvConfigHandler())
            .addHandler(DatabaseConfigHandler(connectionPoolManager))
            .addHandler(ServerConfigHandler())
            .addHandler(NodesConfigHandler())
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(DatabaseModule(eventManager))
    }
}
