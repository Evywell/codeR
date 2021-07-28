package fr.rob.cli.network.netty

import fr.rob.client.network.Client
import fr.rob.client.network.ClientHandler
import fr.rob.core.network.Packet
import fr.rob.core.opcode.OpcodeHandler

class CliClientHandler(private val opcodeHandler: OpcodeHandler, client: Client) : ClientHandler(client) {

    override fun processPacket(opcode: Int, packet: Packet) {
        opcodeHandler.process(opcode, client.session, packet)
    }
}
