package fr.rob.orchestrator.agent

import com.google.protobuf.Any
import com.google.protobuf.Message
import fr.rob.client.network.Client
import fr.rob.core.network.Packet
import fr.rob.entities.orchestrator.AuthenticationAgentProto
import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication.AgentType
import fr.rob.entities.orchestrator.CreateMapInstanceRequestProto.CreateMapInstanceRequest
import fr.rob.entities.orchestrator.RequestProto
import fr.rob.orchestrator.agent.network.ClientHandler
import fr.rob.orchestrator.agent.opcode.AgentOpcodeHandler
import fr.rob.orchestrator.opcode.ServerOpcodeOrchestrator.Companion.AUTHENTICATE_SESSION
import fr.rob.orchestrator.opcode.ServerOpcodeOrchestrator.Companion.CREATE_MAP_INSTANCE
import org.apache.commons.lang3.RandomStringUtils

abstract class AbstractAgent(
    protected val client: Client,
    private val token: String,
    private val agentName: String,
    private val agentType: AgentType
) {

    private val requests = HashMap<String, Message>()

    init {
        client.clientHandler = ClientHandler(client, AgentOpcodeHandler(client.logger))
    }

    fun authenticate() {
        client.open()

        while (!client.isOpen) {
            Thread.sleep(100)
        }

        client.send(createAuthPacket(token))
    }

    fun sendCreateMapInstanceRequest(request: CreateMapInstanceRequest) {
        sendRequest(CREATE_MAP_INSTANCE, request)
    }

    private fun sendRequest(opcode: Int, request: Message) {
        val id = generateRequestId()
        val data = Any.pack(request)
        val message = RequestProto.Request.newBuilder()
            .setId(id)
            .setData(data)
            .build()

        requests[id] = message
        client.send(Packet(opcode, message.toByteArray()))
    }

    private fun generateRequestId(): String {
        return agentName + "-" + RandomStringUtils.randomAlphanumeric(ID_LENGTH)
    }

    private fun createAuthPacket(token: String): Packet {
        val message = AuthenticationAgentProto.Authentication.newBuilder()
            .setToken(token)
            .setType(agentType)
            .build()
            .toByteArray()

        return Packet(AUTHENTICATE_SESSION, message)
    }

    companion object {
        const val ID_LENGTH = 8
    }
}
