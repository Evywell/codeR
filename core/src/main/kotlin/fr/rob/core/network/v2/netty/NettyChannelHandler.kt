package fr.rob.core.network.v2.netty

import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.session.SessionSocketInterface
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.ReferenceCountUtil

abstract class NettyChannelHandler<T>(
    protected val server: ServerInterface<T>
) : ChannelInboundHandlerAdapter() {

    abstract fun createPacketFromMessage(msg: Any): T
    abstract fun createSessionSocket(ctx: ChannelHandlerContext): SessionSocketInterface

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            val channelId = getSessionIdentifier(ctx)
            val session = server.sessionFromIdentifier(channelId)

            server.onPacketReceived(session, createPacketFromMessage(msg))
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

        val session = server.createSession(createSessionSocket(ctx))

        server.onNewConnection(channelId, session)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        cause?.printStackTrace()
    }

    private fun getSessionIdentifier(ctx: ChannelHandlerContext): String =
        ctx.channel().id().asLongText()
}
