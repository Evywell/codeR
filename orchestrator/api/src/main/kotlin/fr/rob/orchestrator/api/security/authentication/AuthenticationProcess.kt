package fr.rob.orchestrator.api.security.authentication

import fr.rob.core.network.v2.session.Session
import fr.rob.orchestrator.shared.Orchestrator
import fr.rob.core.security.authentication.AuthenticationProcess as BaseAuthenticationProcess
import fr.rob.orchestrator.shared.entities.AuthenticationProto.Authentication as AuthenticationMessage

class AuthenticationProcess(private val orchestrator: Orchestrator) :
    BaseAuthenticationProcess() {

    override fun checkAuthentication(authMessage: Any): AuthenticationState {
        authMessage as AuthenticationMessage

        val isAuthenticated = orchestrator.token == authMessage.token
        var error: String? = null

        if (!isAuthenticated) {
            error = ERR_WRONG_TOKEN
        }

        return OrchestratorAuthenticationState(isAuthenticated, error)
    }

    override fun authenticate(session: Session, authMessage: Any): AuthenticationState {
        authMessage as AuthenticationMessage
        val state = checkAuthentication(authMessage)

        if (!state.isAuthenticated) {
            return state
        }

        session.isAuthenticated = true

        return state
    }

    companion object {
        const val ERR_WRONG_TOKEN = "err_wrong_token"
    }

    data class OrchestratorAuthenticationState(
        override var isAuthenticated: Boolean,
        override var error: String? = null
    ) : AuthenticationState
}
