package fr.rob.login.security.authentication.attempt

import fr.rob.core.network.session.Session
import fr.rob.core.security.attempt.SecurityAttemptContextInterface
import fr.rob.login.security.account.AccountProcess

class AuthAttemptContext(
    private val session: Session,
    private val userId: Int,
    private val accountProcess: AccountProcess
) : SecurityAttemptContextInterface {

    override fun getName(): String = CONTEXT_AUTHENTICATION

    override fun getSubject(): String = userId.toString()
    override fun getSession(): Session = session

    override fun getMaxAttempt(): Int = MAX_ATTEMPTS

    override fun execute() {
        accountProcess.lockByUserId(userId)
    }

    companion object {
        const val CONTEXT_AUTHENTICATION = "AuthenticationContext"
        const val MAX_ATTEMPTS = 8
    }
}
