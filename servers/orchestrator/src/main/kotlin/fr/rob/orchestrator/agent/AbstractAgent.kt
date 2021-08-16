package fr.rob.orchestrator.agent

import com.google.protobuf.Message
import fr.rob.client.network.Client
import fr.rob.entities.orchestrator.AuthenticationAgentProto
import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication.AgentType
import fr.rob.entities.orchestrator.CreateMapInstanceRequestProto.CreateMapInstanceRequest
import fr.rob.orchestrator.agent.exception.OrchestratorAgentException
import fr.rob.orchestrator.agent.network.ClientHandler
import fr.rob.orchestrator.agent.opcode.AgentOpcodeHandler
import fr.rob.orchestrator.opcode.ServerOpcodeOrchestrator.Companion.AUTHENTICATE_SESSION

abstract class AbstractAgent(
    protected val client: Client,
    private val token: String,
    private val agentType: AgentType
) {

    init {
        val opcodeHandler = AgentOpcodeHandler(client, client.logger)
        opcodeHandler.initialize()

        client.clientHandler = ClientHandler(client, opcodeHandler)
    }

    fun authenticate() {
        client.open()

        if (!(sendSyncMessage(AUTHENTICATE_SESSION, createAuthMessage(token)) as Boolean)) {
            throw OrchestratorAgentException("Cannot authenticate to orchestrator")
        }
    }

    fun sendCreateMapInstanceRequest(request: CreateMapInstanceRequest) {
        // sendSyncMessage(CREATE_MAP_INSTANCE, request)
    }

    private fun sendSyncMessage(opcode: Int, message: Message): Any? {
        val request = client.createRequest(message)

        return client.sendSync(opcode, request)
    }

    private fun createAuthMessage(token: String): AuthenticationAgentProto.Authentication =
        AuthenticationAgentProto.Authentication.newBuilder()
            .setToken(token)
            .setType(agentType)
            .build()
}
