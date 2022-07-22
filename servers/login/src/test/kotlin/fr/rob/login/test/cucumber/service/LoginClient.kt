package fr.rob.login.test.cucumber.service

import fr.rob.core.network.v2.session.Session
import fr.rob.core.test.cucumber.service.Client
import fr.rob.core.test.cucumber.service.LocalSessionSocket
import fr.rob.core.test.cucumber.service.network.MessageReceiverInterface
import fr.rob.login.LoginApplication
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.network.LoginSession
import fr.rob.login.test.cucumber.fixtures.accounts
import fr.rob.login.test.cucumber.service.network.LoginMessageReceiver

class LoginClient(private val app: LoginApplication) : Client() {

    fun authenticateToServerAs(accountId: Int) {
        session.accountId = accountId
        session.isAuthenticated = true

        val accountName = accounts[accountId] ?: "Unknown#0000"

        app.processManager.getOrMakeProcess(SessionInitializerProcess::class)
            .execute(session as LoginSession, accountName)
    }

    override fun createMessageReceiver(): MessageReceiverInterface = LoginMessageReceiver()

    override fun createSession(): Session = LoginSession(LocalSessionSocket(this))
}
