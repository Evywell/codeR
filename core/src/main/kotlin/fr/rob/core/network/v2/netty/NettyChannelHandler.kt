package fr.rob.core.network.v2.netty

import fr.rob.core.network.netty.NettyPacket
import fr.rob.core.network.v2.ServerInterface
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.ReferenceCountUtil

class NettyChannelHandler(private val server: ServerInterface) : ChannelInboundHandlerAdapter() {

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            val channelId = getSessionIdentifier(ctx)
            val session = server.sessionFromIdentifier(channelId)
            val packet = NettyPacket.fromByteArray(msg as ByteArray)

            server.onPacketReceived(session, packet)
        } catch (exception: Exception) {
            // TODO: use a logger instead
            exception.printStackTrace()

            if (ctx.channel().isOpen) {
                ctx.channel().close()
            }
        } finally {
            ReferenceCountUtil.release(msg)
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        val channelId = getSessionIdentifier(ctx)

        val session = server.createSession()
        session.socket = NettySessionSocket(ctx.channel())

        server.onNewConnection(channelId, session)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        cause?.printStackTrace()
    }

    private fun getSessionIdentifier(ctx: ChannelHandlerContext): String =
        ctx.channel().id().toString()
}
