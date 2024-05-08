package fr.rob.core.network.v2.netty.client

import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.session.SessionSocketInterface
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.ReferenceCountUtil

abstract class NettyChannelHandler<T>(protected val client: ClientInterface<T>) : ChannelInboundHandlerAdapter() {

    abstract fun createPacketFromMessage(msg: Any): T
    abstract fun createSessionSocket(ctx: ChannelHandlerContext): SessionSocketInterface

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            client.onPacketReceived(createPacketFromMessage(msg))
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
        val session = client.createSession(createSessionSocket(ctx))

        client.onConnectionEstablished(session)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}
