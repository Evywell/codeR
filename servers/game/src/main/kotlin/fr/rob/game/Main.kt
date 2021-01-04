package fr.rob.game

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.initiator.Initiator
import fr.rob.game.application.network.GameServerManager
import fr.rob.game.application.setup.AppSetup
import fr.rob.game.application.setup.tasks.TaskAuthCollectJWTPublicKey
import fr.rob.game.application.setup.tasks.TaskLoadServerConfig
import fr.rob.game.domain.server.ServerManager
import fr.rob.game.domain.setup.Setup
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

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val configPath: URL = ResourceManager.getResourceURL("config.json")
                ?: throw RuntimeException("Cannot find config.json")

            val app = Main()
            app
                .addConfigPath("default", configPath)
                .handler(DatabaseConfigHandler(app.connectionManager))
                .handler(ServerConfigHandler())

            app.run()
        }
    }

    override fun run() {
        super.run()
        initiator
            .runTask(TASK_AUTH_COLLECT_JWT_PUBLIC_KEY) // Retrieve and store JWTPublicKey
            .runTask(TASK_LOAD_SERVER_CONFIG) // Store server info

        val serverManager: ServerManager = GameServerManager()
        serverManager.buildGameServers(setup.getServers())
    }

    override fun registerInitiatorTasks(initiator: Initiator) {
        initiator
            .addTask(TASK_AUTH_COLLECT_JWT_PUBLIC_KEY, TaskAuthCollectJWTPublicKey(setup))
            .addTask(TASK_LOAD_SERVER_CONFIG, TaskLoadServerConfig(this, setup))
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules
            .add(DatabaseModule(eventManager))
    }
}
