package fr.rob.game.domain.network.netty

import fr.rob.game.domain.log.LoggableException
import fr.rob.game.domain.network.packet.Packet
import fr.rob.game.domain.network.session.AppSession
import fr.rob.game.domain.network.session.Session
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class NettyGameServerHandler(private val nettyGameServer: NettyGameServer) : ChannelInboundHandlerAdapter() {

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            val session: Session = nettyGameServer.sessionFromIdentifier(ctx.channel().hashCode())
            val buffer: ByteBuf = Unpooled.copiedBuffer(msg as ByteArray)
            val bytes: ByteArray

            if (buffer.hasArray()) {
                bytes = buffer.array()
            } else {
                val length: Int = buffer.readableBytes()
                bytes = ByteArray(length)
                buffer.getBytes(buffer.readerIndex(), bytes)
            }

            val packet = Packet.fromByteArray(bytes)
            val opcode = packet.readOpcode()

            nettyGameServer.clientOpcodeHandler.process(opcode, session, packet)
        } catch (exception: Exception) {
            if (exception is LoggableException) {
                exception.message?.let { nettyGameServer.logger.error(it) }
            }

            if (ctx.channel().isOpen) {
                ctx.channel().close()
            }
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        nettyGameServer.registerSession(ctx.channel().hashCode(), AppSession(nettyGameServer, ctx.channel()))
    }

    // @todo: Add channelInactive and destroy the session
}
