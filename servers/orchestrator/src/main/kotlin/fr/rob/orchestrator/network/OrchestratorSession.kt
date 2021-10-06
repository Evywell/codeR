package fr.rob.orchestrator.network

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication.AgentType
import fr.rob.entities.orchestrator.CreateMapInstanceRequestProto
import fr.rob.orchestrator.opcode.ServerOpcodeOrchestrator.Companion.CREATE_MAP_INSTANCE

class OrchestratorSession : Session() {
    lateinit var agentType: AgentType

    fun requestNewInstance(request: CreateMapInstanceRequestProto.CreateMapInstanceRequest) {
        send(Packet(CREATE_MAP_INSTANCE, request.toByteArray()))
    }
}
