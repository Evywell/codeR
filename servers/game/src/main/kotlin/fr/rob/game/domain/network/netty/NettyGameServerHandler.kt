package fr.rob.game.domain.network.netty

import fr.rob.core.network.Packet
import fr.rob.core.network.netty.NettyServerHandler
import fr.rob.core.network.netty.NettySession
import fr.rob.core.network.session.Session
import fr.rob.game.domain.opcode.ClientOpcodeHandler
import fr.rob.core.opcode.OpcodeHandler
import io.netty.channel.ChannelHandlerContext

class NettyGameServerHandler(private val nettyServer: NettyGameServer) : NettyServerHandler(nettyServer) {

    private val opcodeHandler: OpcodeHandler =
        ClientOpcodeHandler(nettyServer.processManager, nettyServer.loggerFactory.create("opcode"))

    override fun processPacket(opcode: Int, session: Session, packet: Packet) {
        opcodeHandler.process(opcode, session, packet)
    }
}
