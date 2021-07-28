package fr.rob.orchestrator.agent.network

import fr.rob.client.network.Client
import fr.rob.core.network.Packet
import fr.rob.orchestrator.agent.opcode.AgentOpcodeHandler
import fr.rob.client.network.ClientHandler as BaseClientHandler

class ClientHandler(client: Client, private val handler: AgentOpcodeHandler) : BaseClientHandler(client) {

    override fun processPacket(opcode: Int, packet: Packet) {
        handler.process(opcode, client.session, packet)
    }
}
