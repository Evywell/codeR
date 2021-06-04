package fr.rob.login

import fr.rob.core.AbstractModule
import fr.rob.core.BaseApplication
import fr.rob.core.config.Config
import fr.rob.core.database.ConnectionManager
import fr.rob.core.event.EventManager
import fr.rob.core.initiator.Initiator
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.process.ProcessManager
import fr.rob.login.config.DatabaseConfigHandler
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.game.character.CharacterRepository
import fr.rob.login.game.character.create.CharacterCreateProcess
import fr.rob.login.game.character.stand.CharacterStandProcess
import fr.rob.login.game.character.stand.CharacterStandRepository
import fr.rob.login.network.netty.NettyLoginServer
import fr.rob.login.security.SecurityModule

open class LoginApplication(private val loggerFactory: LoggerFactoryInterface, env: String) : BaseApplication(env) {

    private val eventManager = EventManager()
    val connectionManager = ConnectionManager(eventManager)
    val processManager = ProcessManager()

    override fun run() {
        super.run()

        config!!.retrieveConfig(CONFIG_KEY_DATABASES)

        val characterRepository = CharacterRepository(connectionManager.getConnection(DB_PLAYERS)!!)

        processManager.registerProcess(CharacterStandProcess::class) {
            CharacterStandProcess(CharacterStandRepository(connectionManager.getConnection(DB_PLAYERS)!!))
        }

        processManager.registerProcess(CharacterCreateProcess::class) {
            CharacterCreateProcess(characterRepository)
        }

        processManager.registerProcess(SessionInitializerProcess::class) {
            SessionInitializerProcess(characterRepository)
        }

        runServer()
    }

    open fun runServer() {
        val loginNode = NettyLoginServer(
            this, loggerFactory,
            LOGIN_SERVER_PORT,
            LOGIN_SERVER_ENABLE_SSL
        )

        loginNode.start()
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(SecurityModule(env, processManager))
    }

    override fun registerInitiatorTasks(initiator: Initiator) {}

    override fun registerConfigHandlers(config: Config) {
        config
            .addHandler(DatabaseConfigHandler(connectionManager))
    }
}
