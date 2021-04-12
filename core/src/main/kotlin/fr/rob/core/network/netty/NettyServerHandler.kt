package fr.rob.core.network.netty

import fr.rob.core.log.LoggableException
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

abstract class NettyServerHandler(private val nettyServer: NettyServer) : ChannelInboundHandlerAdapter() {

    abstract fun processPacket(opcode: Int, session: Session, packet: Packet)

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            val session: Session = nettyServer.sessionFromIdentifier(ctx.channel().hashCode())
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

            processPacket(opcode, session, packet)
        } catch (exception: Exception) {
            if (exception is LoggableException) {
                exception.message?.let { exception.logger.error(it) }
            }

            if (ctx.channel().isOpen) {
                ctx.channel().close()
            }
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        nettyServer.registerSession(ctx.channel().hashCode(), NettySession(ctx.channel(), nettyServer.logger))
    }

    // @todo: Add channelInactive and destroy the session
}
