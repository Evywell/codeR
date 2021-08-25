package fr.rob.orchestrator.test.unit.agent

import fr.rob.core.network.message.ResponseStack
import fr.rob.entities.orchestrator.AuthenticationAgentProto
import fr.rob.orchestrator.agent.authentication.AuthenticationResultOpcode
import fr.rob.orchestrator.network.OrchestratorSession
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AuthenticationResultOpcodeTest {

    @Test
    fun `authentication opcode should return positive when authentication is success`() {
        // Arrange
        val responseStack = ResponseStack()
        val opcode = AuthenticationResultOpcode(responseStack)
        val session = OrchestratorSession()
        val response = AuthenticationAgentProto.AuthenticationResult.newBuilder()
            .setResult(true)
            .build()

        // Act
        val result = opcode.handleResponse(session, response)

        // Assert
        assertTrue(opcode.getDataType() is AuthenticationAgentProto.AuthenticationResult)
        assertTrue(result)
        assertTrue(session.isAuthenticated)
    }

    @Test
    fun `authentication opcode should return negative when authentication is error`() {
        // Arrange
        val responseStack = ResponseStack()
        val opcode = AuthenticationResultOpcode(responseStack)
        val session = OrchestratorSession()
        val response = AuthenticationAgentProto.AuthenticationResult.newBuilder()
            .setResult(false)
            .build()

        // Act
        val result = opcode.handleResponse(session, response)

        // Assert
        assertTrue(opcode.getDataType() is AuthenticationAgentProto.AuthenticationResult)
        assertFalse(result)
        assertFalse(session.isAuthenticated)
    }
}
