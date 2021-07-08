package fr.rob.core.network.netty.plugin.security

import fr.rob.core.event.EventInterface
import fr.rob.core.event.EventListenerInterface
import fr.rob.core.network.netty.event.NettySessionKickedEvent
import fr.rob.core.security.attempt.SecurityAttemptProcess

class KickLimiterListener(private val securityAttemptProcess: SecurityAttemptProcess) : EventListenerInterface {

    override fun process(event: EventInterface) {
        event as NettySessionKickedEvent

        securityAttemptProcess.execute(KickContext(event.session))
    }
}
