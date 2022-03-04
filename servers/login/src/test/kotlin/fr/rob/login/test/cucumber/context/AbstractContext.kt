package fr.rob.login.test.cucumber.context

import fr.rob.core.ENV_DEV
import fr.rob.core.config.Config
import fr.rob.core.config.hashmap.HashMapConfig
import fr.rob.core.event.EventManager
import fr.rob.core.test.cucumber.service.Server
import fr.rob.core.test.unit.sandbox.log.NILogger
import fr.rob.login.LoginApplication
import fr.rob.login.opcode.LoginOpcodeHandler
import fr.rob.login.test.cucumber.service.LoginClient
import fr.rob.login.test.cucumber.service.log.NullLoggerFactory
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.spy
import fr.rob.core.test.cucumber.AbstractContext as BaseAbstractContext

abstract class AbstractContext : BaseAbstractContext() {

    private val clients = HashMap<String, LoginClient>()
    private var server: Server
    private val eventManager = EventManager()
    protected val app: LoginApplication = spy(LoginApplication(NullLoggerFactory(), ENV_DEV))

    init {
        // Initialize app
        val config = createConfig()

        app.config = config

        val logger = NILogger()

        // Initialize opcode handler
        val opcodeHandler = LoginOpcodeHandler(ENV_DEV, app.processManager, eventManager, logger)

        server = Server(opcodeHandler)

        doReturn(server).`when`(app).createServer()

        app.run()

        opcodeHandler.initialize()

        // Initialize the main client
        val mainClient = LoginClient(app)

        clients[CLIENT_MAIN] = mainClient

        do {
            var allClientsAreReady = true

            for ((_, client) in clients) {
                if (!client.isReady) {
                    allClientsAreReady = false
                }
            }

            Thread.sleep(100)
        } while (!allClientsAreReady)

        mainClient.connectToServer(server)
    }

    fun getMainClient(): LoginClient = clients[CLIENT_MAIN]!!

    fun close() {
        for ((_, client) in clients) {
            client.stop()
        }

        server.stop()
    }

    private fun createConfig(): Config {
        val config = HashMapConfig()

        config.properties["databases.players.host"] = "mysql_game"
        config.properties["databases.players.port"] = 3306L
        config.properties["databases.players.user"] = "testing"
        config.properties["databases.players.password"] = "passwordtesting"
        config.properties["databases.players.database"] = "players"

        config.properties["databases.config.host"] = "mysql_game"
        config.properties["databases.config.port"] = 3306L
        config.properties["databases.config.user"] = "testing"
        config.properties["databases.config.password"] = "passwordtesting"
        config.properties["databases.config.database"] = "config"

        config.properties["orchestrator.id"] = 1

        return config
    }

    companion object {
        const val CLIENT_MAIN = "main"
    }
}
