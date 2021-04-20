package fr.rob.login.network.netty

import fr.rob.core.network.Packet
import fr.rob.core.network.netty.NettyServerHandler
import fr.rob.core.network.session.Session

class NettyLoginServerHandler(private val nettyServer: NettyLoginServer) : NettyServerHandler(nettyServer) {

    override fun processPacket(opcode: Int, session: Session, packet: Packet) {
        nettyServer.opcodeHandler.process(opcode, session, packet)
    }
}
