package fr.rob.login.test.cucumber.context

import fr.rob.core.database.Connection
import fr.rob.login.DB_PLAYERS

class LoginContext : AbstractContext() {

    fun authAs(userId: Int) {
        getMainClient().authenticateToServerAs(userId)
    }

    fun getPlayersDatabase(): Connection {
        return (app.connectionPoolManager.getPool(DB_PLAYERS) ?: throw Exception("Cannot retrieve players database pool"))
            .getNextConnection(0)
    }
}
