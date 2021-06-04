package fr.rob.login.test.cucumber.service

import fr.rob.core.test.cucumber.service.Client
import fr.rob.core.test.cucumber.service.network.MessageReceiverInterface
import fr.rob.login.LoginApplication
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.network.LoginSessionData
import fr.rob.login.test.cucumber.service.network.LoginMessageReceiver

class LoginClient(private val app: LoginApplication) : Client() {

    fun authenticateToServerAs(userId: Int) {
        session.userId = userId
        session.isAuthenticated = true
        session.data = LoginSessionData()

        app.processManager.getOrMakeProcess(SessionInitializerProcess::class).execute(session)
    }

    override fun createMessageReceiver(): MessageReceiverInterface = LoginMessageReceiver()
}
