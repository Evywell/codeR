package fr.rob.login

import fr.raven.log.LoggerFactoryInterface
import fr.rob.core.AbstractModule
import fr.rob.core.DB_CONFIG
import fr.rob.core.SingleServerApplication
import fr.rob.core.config.Config
import fr.rob.core.config.commons.configuration2.ConfigLoader
import fr.rob.core.database.ConnectionManager
import fr.rob.core.database.pool.ConnectionPoolManager
import fr.rob.core.event.EventManager
import fr.rob.core.initiator.Initiator
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.netty.basic.BasicNettyServer
import fr.rob.core.network.v2.netty.builder.NettySessionSocketBuilder
import fr.rob.core.process.ProcessManager
import fr.rob.core.security.SecurityBanProcess
import fr.rob.core.security.SecurityBanRepository
import fr.rob.core.security.attempt.SecurityAttemptProcess
import fr.rob.login.config.DatabaseConfigHandler
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.game.character.CharacterRepository
import fr.rob.login.game.character.create.CharacterCreateProcess
import fr.rob.login.game.character.stand.CharacterStandProcess
import fr.rob.login.game.character.stand.CharacterStandRepository
import fr.rob.login.network.LoginServer
import fr.rob.login.opcode.LoginOpcodeHandler
import fr.rob.login.security.SecurityModule
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.account.AccountRepository

open class LoginApplication(private val loggerFactory: LoggerFactoryInterface, env: String) :
    SingleServerApplication<Packet>(env, loggerFactory.create("login"), ConfigLoader(), EventManager()) {

    val connectionManager = ConnectionManager(eventManager)
    var connectionPoolManager = ConnectionPoolManager(4, connectionManager)
    val processManager = ProcessManager()

    override fun initDependencies() {
        super.initDependencies()

        config!!.retrieveConfig(CONFIG_KEY_DATABASES)

        val dbPlayersPool = connectionPoolManager.getPool(DB_PLAYERS)!!
        val dbConfigPool = connectionPoolManager.getPool(DB_CONFIG)!!

        val characterRepository = CharacterRepository(dbPlayersPool.getNextConnection())
        val accountRepository = AccountRepository(dbPlayersPool.getNextConnection())
        val securityBanRepository = SecurityBanRepository(dbPlayersPool.getNextConnection())

        processManager.registerProcess(CharacterStandProcess::class) {
            CharacterStandProcess(CharacterStandRepository(dbPlayersPool.getNextConnection()))
        }

        processManager.registerProcess(CharacterCreateProcess::class) {
            CharacterCreateProcess(characterRepository)
        }

        processManager.registerProcess(AccountProcess::class) {
            AccountProcess(accountRepository)
        }

        processManager.registerProcess(SessionInitializerProcess::class) {
            SessionInitializerProcess(characterRepository, processManager.getOrMakeProcess(AccountProcess::class))
        }

        processManager.registerProcess(SecurityBanProcess::class) {
            SecurityBanProcess(securityBanRepository)
        }

        processManager.registerProcess(SecurityAttemptProcess::class) {
            SecurityAttemptProcess(processManager.getOrMakeProcess(SecurityBanProcess::class))
        }
    }

    override fun afterRun() {
        val socketBuilder = NettySessionSocketBuilder()
        val process = BasicNettyServer(LOGIN_SERVER_PORT, server, socketBuilder, LOGIN_SERVER_ENABLE_SSL)
        server.start(process)
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(SecurityModule(env, processManager))
    }

    override fun registerInitiatorTasks(initiator: Initiator) {}

    override fun registerConfigHandlers(config: Config) {
        config
            .addHandler(DatabaseConfigHandler(connectionPoolManager))
    }

    override fun createServer(): Server<Packet> {
        val opcodeHandler = LoginOpcodeHandler(env, processManager, eventManager, loggerFactory.create("opcode"))

        return LoginServer(opcodeHandler)
    }
}
