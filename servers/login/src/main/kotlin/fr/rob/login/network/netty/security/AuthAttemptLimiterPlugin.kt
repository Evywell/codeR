package fr.rob.login.network.netty.security

import fr.rob.core.network.netty.NettyPlugin
import fr.rob.core.network.netty.NettyServer
import fr.rob.core.security.attempt.SecurityAttemptProcess
import fr.rob.login.security.account.AccountProcess
import fr.rob.login.security.authentication.attempt.AuthAttemptLimiterListener
import fr.rob.login.security.authentication.attempt.event.AuthenticationFailAttemptEvent

class AuthAttemptLimiterPlugin(
    private val securityAttemptProcess: SecurityAttemptProcess,
    private val accountProcess: AccountProcess
) : NettyPlugin() {

    override fun boot(nettyServer: NettyServer) {
        val listener = AuthAttemptLimiterListener(securityAttemptProcess, accountProcess)

        nettyServer.registerListener(AuthenticationFailAttemptEvent.AUTHENTICATION_FAIL_ATTEMPT, listener)
    }
}
