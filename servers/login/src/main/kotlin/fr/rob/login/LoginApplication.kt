package fr.rob.login

import fr.rob.core.AbstractModule
import fr.rob.core.DB_CONFIG
import fr.rob.core.SingleServerApplication
import fr.rob.core.config.Config
import fr.rob.core.config.commons.configuration2.ConfigLoader
import fr.rob.core.database.ConnectionManager
import fr.rob.core.event.EventManager
import fr.rob.core.initiator.Initiator
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.network.Server
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
import fr.rob.login.network.netty.NettyLoginServer
import fr.rob.login.security.OrchestratorModule
import fr.rob.login.security.SecurityModule
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.account.AccountRepository
import fr.rob.login.security.strategy.StrategyProcess
import fr.rob.orchestrator.agent.AbstractAgent
import fr.rob.shared.orchestrator.OrchestratorRepository
import fr.rob.shared.orchestrator.OrchestratorRepositoryInterface
import fr.rob.shared.orchestrator.config.OrchestratorConfigHandler

open class LoginApplication(private val loggerFactory: LoggerFactoryInterface, env: String) :
    SingleServerApplication(env, loggerFactory.create("login"), ConfigLoader(), EventManager()) {

    val connectionManager = ConnectionManager(eventManager)
    val processManager = ProcessManager()

    private lateinit var orchestratorRepository: OrchestratorRepositoryInterface

    override fun initDependencies() {
        super.initDependencies()

        config!!.retrieveConfig(CONFIG_KEY_DATABASES)

        val dbPlayers = connectionManager.getConnection(DB_PLAYERS)!!
        val dbConfig = connectionManager.getConnection(DB_CONFIG)!!

        val characterRepository = CharacterRepository(dbPlayers)
        val accountRepository = AccountRepository(dbPlayers)
        val securityBanRepository = SecurityBanRepository(dbPlayers)
        orchestratorRepository = OrchestratorRepository(dbConfig)

        processManager.registerProcess(CharacterStandProcess::class) {
            CharacterStandProcess(CharacterStandRepository(dbPlayers))
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
        processManager.registerProcess(StrategyProcess::class) {
            StrategyProcess(server)
        }

        val agent = processManager.getOrMakeProcess(AbstractAgent::class)
        agent.authenticate()

        server.start()
    }

    override fun registerModules(modules: MutableList<AbstractModule>) {
        modules.add(SecurityModule(env, processManager, server))
        modules.add(
            OrchestratorModule(
                orchestratorRepository,
                config!!.retrieveConfig("orchestrator") as OrchestratorConfigHandler.OrchestratorConfig,
                processManager,
                loggerFactory
            )
        )
    }

    override fun registerInitiatorTasks(initiator: Initiator) {}

    override fun registerConfigHandlers(config: Config) {
        config
            .addHandler(DatabaseConfigHandler(connectionManager))
            .addHandler(OrchestratorConfigHandler())
    }

    override fun createServer(): Server = NettyLoginServer(
        this,
        eventManager,
        null,
        loggerFactory,
        LOGIN_SERVER_PORT,
        LOGIN_SERVER_ENABLE_SSL
    )
}
