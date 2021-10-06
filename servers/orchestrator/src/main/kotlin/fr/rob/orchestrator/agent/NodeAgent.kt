package fr.rob.orchestrator.agent

import fr.rob.client.network.ClientInterface
import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.Packet
import fr.rob.entities.orchestrator.AuthenticationAgentProto.Authentication.AgentType.NODE
import fr.rob.entities.orchestrator.NewGameNodeProto
import fr.rob.orchestrator.opcode.ServerOpcodeOrchestrator

class NodeAgent(client: ClientInterface, token: String, logger: LoggerInterface) :
    AbstractAgent(client, token, NODE, logger) {

    fun registerNewGameNode(name: String, port: Int) {
        val msg = NewGameNodeProto.NewGameNode.newBuilder()
            .setName(name)
            .setPort(port)
            .build()
            .toByteArray()

        client.send(Packet(ServerOpcodeOrchestrator.NEW_GAME_NODE, msg))
    }
}
