package fr.rob.login.network.netty

import fr.rob.core.network.Packet
import fr.rob.core.network.netty.NettyServerHandler
import fr.rob.core.network.session.Session
import io.netty.channel.ChannelHandlerContext

class NettyLoginServerHandler(private val nettyServer: NettyLoginServer) : NettyServerHandler(nettyServer) {

    override fun processPacket(opcode: Int, session: Session, packet: Packet) {
        nettyServer.opcodeHandler.process(opcode, session, packet)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}
