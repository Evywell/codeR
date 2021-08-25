package fr.rob.orchestrator.security.authentication

import fr.rob.core.network.session.Session
import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication
import fr.rob.orchestrator.network.OrchestratorSession
import fr.rob.shared.orchestrator.Orchestrator
import fr.rob.core.security.authentication.AuthenticationProcess as BaseAuthenticationProcess

class AuthenticationProcess(private val orchestrator: Orchestrator) :
    BaseAuthenticationProcess() {

    override fun checkAuthentication(authMessage: Any): AuthenticationState {
        authMessage as Authentication

        val isAuthenticated = orchestrator.token == authMessage.token
        var error: String? = null

        if (!isAuthenticated) {
            error = ERR_WRONG_TOKEN
        }

        return OrchestratorAuthenticationState(isAuthenticated, error)
    }

    override fun authenticate(session: Session, authMessage: Any): AuthenticationState {
        session as OrchestratorSession
        authMessage as Authentication
        val state = checkAuthentication(authMessage)

        if (!state.isAuthenticated) {
            return state
        }

        session.isAuthenticated = true
        session.agentType = authMessage.type

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
