package fr.rob.orchestrator.network.netty

import fr.rob.core.network.Packet
import fr.rob.core.network.netty.NettyServerHandler
import fr.rob.core.network.session.Session

open class OrchestratorServerHandler(private val server: OrchestratorNettyServer) : NettyServerHandler(server) {

    override fun processPacket(opcode: Int, session: Session, packet: Packet) {
        server.opcodeHandler.process(opcode, session, packet)
    }
}
