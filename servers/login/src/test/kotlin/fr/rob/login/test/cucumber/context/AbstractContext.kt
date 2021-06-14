package fr.rob.login.test.cucumber.context

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.spy
import fr.rob.core.ENV_DEV
import fr.rob.core.log.LoggerFactory
import fr.rob.core.test.cucumber.service.Server
import fr.rob.core.test.unit.sandbox.log.NILogger
import fr.rob.login.LoginApplication
import fr.rob.login.opcode.LoginOpcodeHandler
import fr.rob.login.test.cucumber.service.LoginClient
import fr.rob.login.test.cucumber.service.config.Config
import fr.rob.core.test.cucumber.AbstractContext as BaseAbstractContext

abstract class AbstractContext : BaseAbstractContext() {

    private val clients = HashMap<String, LoginClient>()
    private var server: Server
    protected val app: LoginApplication = spy(LoginApplication(LoggerFactory, ENV_DEV))

    init {
        // Initialize app
        val config = createConfig()

        app.config = config

        val logger = NILogger()

        // Initialize opcode handler
        val opcodeHandler = LoginOpcodeHandler(ENV_DEV, app.processManager, logger)

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
        val config = Config()

        config.properties["databases.players.host"] = "mysql_game"
        config.properties["databases.players.port"] = 3306L
        config.properties["databases.players.user"] = "testing"
        config.properties["databases.players.password"] = "passwordtesting"
        config.properties["databases.players.database"] = "players"

        return config
    }

    companion object {
        const val CLIENT_MAIN = "main"
    }
}
