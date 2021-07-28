package fr.rob.game

import fr.rob.core.AbstractModule
import fr.rob.core.MultiServerApplication
import fr.rob.core.config.Config
import fr.rob.core.config.commons.configuration2.ConfigLoader
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
    eventManager: EventManager,
    private val connectionManager: ConnectionManager
) : MultiServerApplication(env, LoggerFactory.create("game"), ConfigLoader(), eventManager) {

    private val processManager = ProcessManager()

    lateinit var servers: Array<Server>

    override fun afterRun() {
        initiator
            .runTask(TASK_LOAD_SERVER_CONFIG) // Store server info

        val serverManager = GameServerManager(NettyGameServerFactory(this, processManager, eventManager))

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

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(DatabaseModule(eventManager))
    }
}
