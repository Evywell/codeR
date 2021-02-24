package fr.rob.game

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.config.Config
import fr.rob.core.initiator.Initiator
import fr.rob.game.domain.network.GameServerManager
import fr.rob.game.domain.network.Server
import fr.rob.game.domain.network.netty.NettyGameServerFactory
import fr.rob.game.domain.process.ProcessManager
import fr.rob.game.domain.security.SecurityModule
import fr.rob.game.domain.setup.AppSetup
import fr.rob.game.domain.setup.Setup
import fr.rob.game.domain.setup.tasks.TaskAuthCollectJWTPublicKey
import fr.rob.game.domain.setup.tasks.TaskLoadServerConfig
import fr.rob.game.domain.setup.tasks.repository.LoadServerRepository
import fr.rob.game.domain.setup.tasks.repository.LoadServerRepositoryInterface
import fr.rob.game.infrastructure.config.ConfigModule
import fr.rob.game.infrastructure.config.EnvConfigHandler
import fr.rob.game.infrastructure.config.ResourceManager
import fr.rob.game.infrastructure.config.database.DatabaseConfigHandler
import fr.rob.game.infrastructure.config.server.ServerConfigHandler
import fr.rob.game.infrastructure.database.ConnectionManager
import fr.rob.game.infrastructure.database.DatabaseModule
import fr.rob.game.infrastructure.event.EventManager
import java.net.URL


class Main : BaseApplication() {

    private val eventManager = EventManager()
    private val setup: Setup = AppSetup()
    private val connectionManager = ConnectionManager(eventManager)
    private val processManager = ProcessManager()

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val configPath: URL = ResourceManager.getResourceURL(CONFIG_FILE)
                ?: throw RuntimeException("Cannot find $CONFIG_FILE")

            val app = Main()
            app
                .addConfigPath("default", configPath)
                .handler(EnvConfigHandler())
                .handler(DatabaseConfigHandler(app.connectionManager))
                .handler(ServerConfigHandler())

            app.run()
        }
    }

    override fun run() {
        super.run()

        if (this.env != ENV_DEV) {
            initiator
                .runTask(TASK_AUTH_COLLECT_JWT_PUBLIC_KEY) // Retrieve and store JWTPublicKey
        }

        initiator
            .runTask(TASK_LOAD_SERVER_CONFIG) // Store server info

        val serverManager = GameServerManager(NettyGameServerFactory(this, processManager))

        serverManager.buildGameServers(setup.getServers())
    }

    override fun registerInitiatorTasks(initiator: Initiator) {
        val config: Config = this.getConfig(CONFIG_DEFAULT)

        @Suppress("UNCHECKED_CAST")
        val servers: Array<Server> = config
            .get(SERVER) as Array<Server>

        val loadServerRepository: LoadServerRepositoryInterface =
            LoadServerRepository(connectionManager.getConnection(DB_CONFIG)
                ?: throw Exception("Cannot get connection $DB_CONFIG")
            )

        initiator
            .addTask(TASK_AUTH_COLLECT_JWT_PUBLIC_KEY, TaskAuthCollectJWTPublicKey(setup))
            .addTask(TASK_LOAD_SERVER_CONFIG, TaskLoadServerConfig(servers, loadServerRepository, setup))
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(ConfigModule(this))
        modules.add(DatabaseModule(eventManager))
        modules.add(SecurityModule(this, setup, processManager))
    }
}
