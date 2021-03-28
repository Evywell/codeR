package fr.rob.game

import com.xenomachina.argparser.ArgParser
import fr.rob.core.config.Config
import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.ENV_DEV
import fr.rob.core.initiator.Initiator
import fr.rob.game.domain.args.GameServerArgs
import fr.rob.game.domain.network.GameServerManager
import fr.rob.game.domain.network.Server
import fr.rob.game.domain.network.netty.NettyGameServerFactory
import fr.rob.core.process.ProcessManager
import fr.rob.game.domain.setup.AppSetup
import fr.rob.game.domain.setup.Setup
import fr.rob.game.domain.setup.tasks.TaskLoadServerConfig
import fr.rob.game.domain.setup.tasks.repository.LoadServerRepository
import fr.rob.game.domain.setup.tasks.repository.LoadServerRepositoryInterface
import fr.rob.game.infrastructure.config.ConfigModule
import fr.rob.game.infrastructure.config.EnvConfigHandler
import fr.rob.core.misc.ResourceManager
import fr.rob.game.infrastructure.config.database.DatabaseConfigHandler
import fr.rob.game.infrastructure.config.server.ServerConfigHandler
import fr.rob.game.infrastructure.database.ConnectionManager
import fr.rob.game.infrastructure.database.DatabaseModule
import fr.rob.game.infrastructure.event.EventManager
import java.io.File
import java.nio.file.Paths


class Main : BaseApplication(ENV_DEV) {

    private val eventManager = EventManager()
    private val setup: Setup = AppSetup()
    private val connectionManager = ConnectionManager(eventManager)
    private val processManager = ProcessManager()

    lateinit var config: Config

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var configFileName: String? = null

            ArgParser(args).parseInto(::GameServerArgs).run {
                // configFileName = "$config"
            }

            val app = GameServerSupervisorApplication(ENV_DEV)
            app
                .loadConfig(getConfigFile(configFileName))
                .addHandler(EnvConfigHandler())
                .addHandler(DatabaseConfigHandler(app.connectionManager))
                .addHandler(ServerConfigHandler())

            app.run()
        }

        private fun getConfigFile(configFileName: String?): File
        {
            if (configFileName != null) {
                return File(configFileName)
            }

            val stream = Main::class.java.classLoader.getResourceAsStream(CONFIG_FILE)!!

            val tmpConfig = File.createTempFile("config", ".tmp", File(Paths.get("").toAbsolutePath().toString()))
            tmpConfig.writeBytes(stream.readAllBytes())

            tmpConfig.deleteOnExit()

            return tmpConfig
        }
    }

    override fun run() {
        super.run()

        initiator
            .runTask(TASK_LOAD_SERVER_CONFIG) // Store server info

        val serverManager = GameServerManager(NettyGameServerFactory(this, processManager))

        serverManager.buildGameServers(setup.getServers())
    }

    override fun registerInitiatorTasks(initiator: Initiator) {
        @Suppress("UNCHECKED_CAST")
        val servers: Array<Server> = config
            .retrieveConfig(SERVER) as Array<Server>

        val loadServerRepository: LoadServerRepositoryInterface =
            LoadServerRepository(
                connectionManager.getConnection(DB_CONFIG)
                    ?: throw Exception("Cannot get connection $DB_CONFIG")
            )

        initiator
            .addTask(TASK_LOAD_SERVER_CONFIG, TaskLoadServerConfig(servers, loadServerRepository, setup))
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(ConfigModule(this))
        modules.add(DatabaseModule(eventManager))
    }
}
