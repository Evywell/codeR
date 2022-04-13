package fr.rob.login.security.authentication

import fr.rob.core.event.EventManagerInterface
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.session.Session
import fr.rob.core.opcode.ProtobufOpcodeFunction
import fr.rob.entities.AuthenticationProto
import fr.rob.login.game.SessionInitializerProcess
import fr.rob.login.network.LoginSession
import fr.rob.login.opcode.ServerOpcodeLogin
import fr.rob.login.security.authentication.attempt.event.AuthenticationFailAttemptEvent

abstract class AuthenticationOpcode(
    private val authenticationProcess: AuthenticationProcess,
    private val sessionInitializerProcess: SessionInitializerProcess,
    private val eventManager: EventManagerInterface
) :
    ProtobufOpcodeFunction(false) {

    override fun call(session: Session, message: Any) {
        session as LoginSession

        val authenticationResult = AuthenticationProto.AuthenticationResult.newBuilder()
        val authState = authenticationProcess.authenticate(session, message)

        if (authState.isAuthenticated) {
            // Load the session data
            sessionInitializerProcess.execute(session, authState.accountName!!)

            authenticationResult.result = AUTHENTICATION_RESULT_SUCCESS
        } else {
            authenticationResult.result = AUTHENTICATION_RESULT_ERROR
            authenticationResult.code = authState.error

            val failAttemptEvent = AuthenticationFailAttemptEvent(session, authState.userId)
            eventManager.dispatch(failAttemptEvent)
        }

        session.send(Packet(ServerOpcodeLogin.AUTHENTICATION_RESULT, authenticationResult.build().toByteArray()))
    }

    companion object {
        const val AUTHENTICATION_RESULT_ERROR = false
        const val AUTHENTICATION_RESULT_SUCCESS = true
    }
}
