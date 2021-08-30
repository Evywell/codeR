package fr.rob.orchestrator.test.unit.security.authentication

import fr.rob.entities.orchestrator.AuthenticationAgentProto
import fr.rob.orchestrator.network.OrchestratorSession
import fr.rob.orchestrator.security.authentication.AuthenticationProcess
import fr.rob.orchestrator.security.authentication.AuthenticationProcess.Companion.ERR_WRONG_TOKEN
import fr.rob.orchestrator.security.authentication.AuthenticationProcess.OrchestratorAuthenticationState
import fr.rob.shared.orchestrator.Orchestrator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AuthenticationProcessTest {

    @Test
    fun `try to authenticate with valid token`() {
        // Arrange
        val token = "T1isIsAT0k3en"
        val session = OrchestratorSession()
        val orchestrator = Orchestrator(1, "localhost", token)

        val authMessage = AuthenticationAgentProto.Authentication.newBuilder()
            .setToken(token)
            .setType(AuthenticationAgentProto.Authentication.AgentType.SINGLE_JOB)
            .build()

        val process = AuthenticationProcess(orchestrator)

        // Act
        val authState = process.authenticate(session, authMessage)

        // Assert
        assertTrue(authState.isAuthenticated)
        assertNull(authState.error)
        assertEquals(AuthenticationAgentProto.Authentication.AgentType.SINGLE_JOB, session.agentType)
    }

    @Test
    fun `try to authenticate with invalid token`() {
        // Arrange
        val token = "T1isIsAT0k3en"
        val session = OrchestratorSession()
        val orchestrator = Orchestrator(1, "localhost", token)

        val authMessage = AuthenticationAgentProto.Authentication.newBuilder()
            .setToken("an_invalid_token")
            .setType(AuthenticationAgentProto.Authentication.AgentType.SINGLE_JOB)
            .build()

        val process = AuthenticationProcess(orchestrator)

        // Act
        val authState = process.authenticate(session, authMessage)

        // Assert
        assertFalse(authState.isAuthenticated)
        assertEquals(ERR_WRONG_TOKEN, authState.error)
    }

    @Test
    fun `getter and setters of auth state`() {
        val state = OrchestratorAuthenticationState(true)

        assertTrue(state.isAuthenticated)
        assertNull(state.error)

        state.isAuthenticated = false
        state.error = "An error message"

        assertFalse(state.isAuthenticated)
        assertEquals("An error message", state.error)
    }
}
