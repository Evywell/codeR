package fr.rob.login.security.authentication.attempt.event

import fr.rob.core.event.Event
import fr.rob.core.network.v2.session.Session

class AuthenticationFailAttemptEvent(val session: Session, val userId: Int?) : Event() {

    override fun getName(): String = AUTHENTICATION_FAIL_ATTEMPT

    companion object {
        const val AUTHENTICATION_FAIL_ATTEMPT = "AuthenticationFailAttemptEvent"
    }
}
