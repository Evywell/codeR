package fr.rob.login.test.feature

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.ENV_TEST
import fr.rob.core.config.Config
import fr.rob.core.database.ConnectionManager
import fr.rob.core.event.EventManager
import fr.rob.core.initiator.Initiator
import fr.rob.core.process.ProcessManager
import fr.rob.login.CONFIG_KEY_DATABASES
import fr.rob.login.DB_PLAYERS
import fr.rob.login.config.DatabaseConfigHandler
import fr.rob.login.game.character.stand.CharacterStandProcess
import fr.rob.login.game.character.stand.CharacterStandRepository
import fr.rob.login.security.SecurityModule
import fr.rob.login.test.feature.service.config.ConfigLoader
import fr.rob.login.test.feature.service.log.Logger
import fr.rob.login.test.feature.service.network.TestLoginServer
import fr.rob.login.test.feature.service.config.Config as TestConfig

class LoginApplication(override val env: String = ENV_TEST) : BaseApplication(env, ConfigLoader()) {

    lateinit var server: TestLoginServer

    private val eventManager = EventManager()
    private val connectionManager = ConnectionManager(eventManager)
    val processManager = ProcessManager()
    val logger = Logger()

    override fun run() {
        initConfig()

        super.run()

        config!!.retrieveConfig(CONFIG_KEY_DATABASES)

        processManager.registerProcess(CharacterStandProcess::class) {
            CharacterStandProcess(CharacterStandRepository(connectionManager.getConnection(DB_PLAYERS)!!))
        }

        server = TestLoginServer(this)
        server.start()
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(SecurityModule(env, processManager))
    }

    override fun registerInitiatorTasks(initiator: Initiator) {}

    override fun registerConfigHandlers(config: Config) {
        config
            .addHandler(DatabaseConfigHandler(connectionManager))
    }

    fun stop() {
        server.stop()
    }

    private fun initConfig() {
        (config as TestConfig).properties["databases.players.host"] = "mysql_game"
        (config as TestConfig).properties["databases.players.port"] = 3306L
        (config as TestConfig).properties["databases.players.user"] = "testing"
        (config as TestConfig).properties["databases.players.password"] = "passwordtesting"
        (config as TestConfig).properties["databases.players.database"] = "players"
    }
}
