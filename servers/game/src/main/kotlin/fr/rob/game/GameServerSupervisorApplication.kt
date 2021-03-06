package fr.rob.game

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.config.Config
import fr.rob.core.database.ConnectionManager
import fr.rob.core.event.EventManager
import fr.rob.core.initiator.Initiator
import fr.rob.core.log.LoggerFactory
import fr.rob.core.process.ProcessManager
import fr.rob.game.domain.network.GameServerManager
import fr.rob.game.domain.network.Server
import fr.rob.game.domain.network.netty.NettyGameServerFactory
import fr.rob.game.domain.tasks.TaskLoadServerConfig
import fr.rob.game.domain.tasks.repository.LoadServerRepository
import fr.rob.game.domain.tasks.repository.LoadServerRepositoryInterface
import fr.rob.game.infrastructure.config.EnvConfigHandler
import fr.rob.game.infrastructure.config.database.DatabaseConfigHandler
import fr.rob.game.infrastructure.config.server.ServerConfigHandler
import fr.rob.game.infrastructure.database.DatabaseModule

class GameServerSupervisorApplication(
    env: String,
    private val eventManager: EventManager,
    private val connectionManager: ConnectionManager
) : BaseApplication(env, LoggerFactory.create("game")) {

    private val processManager = ProcessManager()

    lateinit var servers: Array<Server>

    override fun run() {
        super.run()

        initiator
            .runTask(TASK_LOAD_SERVER_CONFIG) // Store server info

        val serverManager = GameServerManager(NettyGameServerFactory(this, processManager))

        serverManager.buildGameServers(servers)
    }

    override fun registerInitiatorTasks(initiator: Initiator) {
        @Suppress("UNCHECKED_CAST")
        servers = config!!.retrieveConfig(SERVER) as Array<Server>

        // Trigger the handle() of the database config handler
        config!!.retrieveConfig(DATABASE)

        val loadServerRepository: LoadServerRepositoryInterface =
            LoadServerRepository(
                connectionManager.getConnection(DB_CONFIG)
                    ?: throw Exception("Cannot get connection $DB_CONFIG")
            )

        initiator
            .addTask(TASK_LOAD_SERVER_CONFIG, TaskLoadServerConfig(servers, loadServerRepository))
    }

    override fun registerConfigHandlers(config: Config) {
        config
            .addHandler(EnvConfigHandler())
            .addHandler(DatabaseConfigHandler(connectionManager))
            .addHandler(ServerConfigHandler())
    }

    override fun createServer(): fr.rob.core.network.Server? = null

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(DatabaseModule(eventManager))
    }
}
