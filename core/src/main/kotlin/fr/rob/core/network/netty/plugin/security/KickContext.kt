package fr.rob.core.network.netty.plugin.security

import fr.rob.core.network.session.Session
import fr.rob.core.security.attempt.SecurityAttemptContextInterface

class KickContext(private val session: Session) : SecurityAttemptContextInterface {

    override fun getName(): String = CONTEXT_KICK

    override fun getSubject(): String = session.getIp()

    override fun getSession(): Session = session

    override fun getMaxAttempt(): Int = MAX_ATTEMPTS

    override fun execute() {
        // Nothing to implement here
    }

    companion object {
        const val CONTEXT_KICK = "KickContext"
        const val MAX_ATTEMPTS = 8
    }
}
