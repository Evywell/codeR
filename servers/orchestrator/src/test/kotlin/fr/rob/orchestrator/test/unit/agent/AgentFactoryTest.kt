package fr.rob.orchestrator.test.unit.agent

import fr.rob.client.network.Client
import fr.rob.core.test.unit.sandbox.log.NILogger
import fr.rob.orchestrator.agent.AbstractAgent
import fr.rob.orchestrator.agent.AgentFactory
import fr.rob.shared.orchestrator.Orchestrator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AgentFactoryTest {

    @Test
    fun `as a factory I am able to create a single job agent`() {
        // Arrange
        val token = "aSecretToken"
        val orchestrator = Orchestrator(1, "localhost", token)
        val agentFactory = AgentFactory(orchestrator, NILogger())

        // Act
        val agent = agentFactory.createSingleJobAgent("127.0.0.1", 1234)

        val tokenField = AbstractAgent::class.java.getDeclaredField("token")
        tokenField.isAccessible = true

        val clientField = AbstractAgent::class.java.getDeclaredField("client")
        clientField.isAccessible = true

        val hostnameField = Client::class.java.getDeclaredField("hostname")
        hostnameField.isAccessible = true

        val portField = Client::class.java.getDeclaredField("port")
        portField.isAccessible = true

        val client = clientField.get(agent) as Client

        // Assert
        assertEquals("127.0.0.1", hostnameField.get(client) as String)
        assertEquals(1234, portField.getInt(client))
        assertEquals(token, tokenField.get(agent) as String)
    }
}
