package fr.rob.game

import com.google.inject.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.database.ConnectionManager
import fr.rob.core.initiator.Initiator
import fr.rob.game.application.setup.tasks.TaskAuthCollectJWTPublicKey
import fr.rob.game.domain.setup.Setup
import fr.rob.game.application.setup.AppSetup
import fr.rob.game.application.setup.tasks.TaskLoadServerConfig
import fr.rob.game.infrastructure.config.database.DatabaseConfigHandler
import java.lang.RuntimeException
import java.net.URL


class Main : BaseApplication() {

    private var setup: Setup = AppSetup()

    var connectionManager = ConnectionManager()

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val configPath: URL = Main::class.java.classLoader.getResource("config.json")
                ?: throw RuntimeException("Cannot find config.json")

            val app = Main()
            app
                .addConfigPath("default", configPath)
                .handler(DatabaseConfigHandler(app.connectionManager))

            app.run()
        }
    }

    override fun run() {
        super.run()
        initiator.runTask(TASK_AUTH_COLLECT_JWT_PUBLIC_KEY) // Retrieve and store JWTPublicKey
    }

    override fun registerInitiatorTasks(initiator: Initiator) {
        initiator
            .addTask(TASK_AUTH_COLLECT_JWT_PUBLIC_KEY, TaskAuthCollectJWTPublicKey(setup))
            .addTask(TASK_LOAD_SERVER_CONFIG, TaskLoadServerConfig(this))
    }

    override fun registerModules(modules: MutableList<AbstractModule>?) {
        // @todo: Remove this ? Replace by my own module system ?
    }
}