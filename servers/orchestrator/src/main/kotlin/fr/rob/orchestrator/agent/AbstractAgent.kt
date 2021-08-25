package fr.rob.orchestrator.agent

import com.google.protobuf.Message
import fr.rob.client.network.ClientInterface
import fr.rob.core.log.LoggerInterface
import fr.rob.entities.orchestrator.AuthenticationAgentProto
import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication.AgentType
import fr.rob.orchestrator.agent.exception.OrchestratorAgentException
import fr.rob.orchestrator.agent.opcode.AgentOpcodeHandler
import fr.rob.orchestrator.opcode.ServerOpcodeOrchestrator.Companion.AUTHENTICATE_SESSION

abstract class AbstractAgent(
    protected val client: ClientInterface,
    private val token: String,
    private val agentType: AgentType,
    logger: LoggerInterface
) {

    val opcodeHandler = AgentOpcodeHandler(client.responseStack, logger)

    init {
        opcodeHandler.initialize()
    }

    fun authenticate(): Boolean {
        client.open()

        if (!(sendSyncMessage(AUTHENTICATE_SESSION, createAuthMessage(token)) as Boolean)) {
            throw OrchestratorAgentException("Cannot authenticate to orchestrator")
        }

        return true
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
