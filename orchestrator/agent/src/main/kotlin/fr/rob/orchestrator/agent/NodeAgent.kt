package fr.rob.orchestrator.agent

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.ClientProcessInterface
import fr.rob.orchestrator.shared.entities.NewGameNodeProto
import fr.rob.orchestrator.shared.opcode.API_NEW_GAME_NODE

class NodeAgent(client: ClientInterface, clientProcess: ClientProcessInterface) : AbstractAgent(client, clientProcess) {

    fun registerNewGameNode(name: String, port: Int) {
        val msg = NewGameNodeProto.NewGameNode.newBuilder()
            .setName(name)
            .setPort(port)
            .build()

        client.send(Packet(API_NEW_GAME_NODE, msg.toByteArray()))
    }
}
