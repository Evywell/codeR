package fr.rob.orchestrator.test.unit.security.authentication

import fr.rob.entities.orchestrator.AuthenticationAgentProto
import fr.rob.orchestrator.agent.AgentManagerProcess
import fr.rob.orchestrator.network.OrchestratorSession
import fr.rob.orchestrator.opcode.AgentOpcodeOrchestrator.Companion.AUTHENTICATE_SESSION_RESULT
import fr.rob.orchestrator.security.authentication.AuthenticationOpcode
import fr.rob.orchestrator.security.authentication.AuthenticationProcess
import fr.rob.shared.orchestrator.Orchestrator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AuthenticationOpcodeTest {

    @Test
    fun `try to authenticate with valid token`() {
        val token = "T1isIsAT0k3en"
        val session = OrchestratorSession()
        val orchestrator = Orchestrator(1, "localhost", token)
        val authProcess = AuthenticationProcess(orchestrator)
        val agentManagerProcess = AgentManagerProcess()
        val authMessage = AuthenticationAgentProto.Authentication.newBuilder()
            .setToken(token)
            .build()

        val authOpcode = AuthenticationOpcode(authProcess, agentManagerProcess)
        val response = authOpcode.callWithResponse(session, authMessage)

        assertTrue(authOpcode.getDataType() is AuthenticationAgentProto.Authentication)
        assertEquals(AUTHENTICATE_SESSION_RESULT, response.opcode)
        assertTrue((response.message as AuthenticationAgentProto.AuthenticationResult).result)
    }

    @Test
    fun `try to authenticate with invalid token`() {
        val token = "T1isIsAT0k3en"
        val session = OrchestratorSession()
        val orchestrator = Orchestrator(1, "localhost", token)
        val authProcess = AuthenticationProcess(orchestrator)
        val agentManagerProcess = AgentManagerProcess()
        val authMessage = AuthenticationAgentProto.Authentication.newBuilder()
            .setToken("invalid_token")
            .build()

        val authOpcode = AuthenticationOpcode(authProcess, agentManagerProcess)
        val response = authOpcode.callWithResponse(session, authMessage)

        assertEquals(AUTHENTICATE_SESSION_RESULT, response.opcode)
        assertFalse((response.message as AuthenticationAgentProto.AuthenticationResult).result)
    }
}
