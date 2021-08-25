package fr.rob.orchestrator.test.unit.agent

import fr.rob.core.test.unit.BaseTest
import fr.rob.orchestrator.agent.SingleJobAgent
import fr.rob.orchestrator.agent.exception.OrchestratorAgentException
import fr.rob.orchestrator.opcode.ServerOpcodeOrchestrator
import fr.rob.orchestrator.test.unit.sandbox.client.ClientMock
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AgentTest : BaseTest() {

    @Test
    fun `authenticate with a valid token`() {
        // Arrange
        val client = ClientMock()
        // Always return true when try to authenticate. Here we do not test the client
        client.whenSendMessage(ServerOpcodeOrchestrator.AUTHENTICATE_SESSION, true)

        val agent = SingleJobAgent(client, "blablabla", logger)

        // Act
        val result = agent.authenticate()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `authenticate with an invalid token`() {
        // Arrange
        val client = ClientMock()
        // Always return false when try to authenticate. Here we do not test the client
        client.whenSendMessage(ServerOpcodeOrchestrator.AUTHENTICATE_SESSION, false)

        val agent = SingleJobAgent(client, "blablabla", logger)

        // Act & Assert
        assertThrows<OrchestratorAgentException> {
            agent.authenticate()
        }
    }
}
