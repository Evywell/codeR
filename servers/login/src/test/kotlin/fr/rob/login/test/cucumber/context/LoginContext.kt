package fr.rob.login.test.cucumber.context

import fr.rob.core.database.Connection
import fr.rob.core.test.cucumber.service.Message
import fr.rob.login.DB_PLAYERS

class LoginContext(private val orchestratorContext: OrchestratorContext) : AbstractContext() {

    lateinit var latestMessage: Message

    init {
        orchestratorContext
    }

    fun authAs(userId: Int) {
        getMainClient().authenticateToServerAs(userId)
    }

    fun getPlayersDatabase(): Connection {
        return app.connectionManager.getConnection(DB_PLAYERS) ?: throw Exception("Cannot retrieve players database")
    }
}
