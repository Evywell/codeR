package fr.rob.login.security.authentication.attempt

import fr.rob.core.event.EventInterface
import fr.rob.core.event.EventListenerInterface
import fr.rob.core.security.attempt.SecurityAttemptProcess
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.authentication.attempt.event.AuthenticationFailAttemptEvent

class AuthAttemptLimiterListener(
    private val securityAttemptProcess: SecurityAttemptProcess,
    private val accountProcess: AccountProcess
) : EventListenerInterface {

    override fun process(event: EventInterface) {
        event as AuthenticationFailAttemptEvent

        if (event.userId == null) {
            return
        }

        securityAttemptProcess.execute(AuthAttemptContext(event.session, event.userId, accountProcess))
    }
}
