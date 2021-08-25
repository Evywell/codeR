package fr.rob.orchestrator.agent

import fr.rob.client.network.Client
import fr.rob.core.log.LoggerInterface
import fr.rob.orchestrator.agent.network.ClientHandler
import fr.rob.shared.orchestrator.Orchestrator

class AgentFactory(private val orchestrator: Orchestrator, private var logger: LoggerInterface) {

    fun createSingleJobAgent(ip: String, port: Int): SingleJobAgent {
        val client = Client(ip, port)
        val agent = SingleJobAgent(client, orchestrator.token, logger)

        client.clientHandler = ClientHandler(client, agent.opcodeHandler)

        return agent
    }
}
