package fr.rob.orchestrator.security.authentication

import fr.rob.core.network.session.Session
import fr.rob.entities.AuthenticationAgentProto
import fr.rob.core.security.authentication.AuthenticationProcess as BaseAuthenticationProcess

class AuthenticationProcess : BaseAuthenticationProcess() {

    override fun checkAuthentication(authMessage: Any): AuthenticationState {
        authMessage as AuthenticationAgentProto.Authentication

        return OrchestratorAuthenticationState(true)
    }

    override fun authenticate(session: Session, authMessage: Any): AuthenticationState {
        val state = checkAuthentication(authMessage)

        if (!state.isAuthenticated) {
            return state
        }

        session.isAuthenticated = true

        return state
    }

    data class OrchestratorAuthenticationState(
        override var isAuthenticated: Boolean,
        override var error: String? = null
    ) :
        AuthenticationState
}
