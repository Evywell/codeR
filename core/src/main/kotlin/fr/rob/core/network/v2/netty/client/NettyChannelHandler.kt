package fr.rob.core.network.v2.netty.client

import fr.rob.core.network.netty.NettyPacket
import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.netty.NettySessionSocket
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

class NettyChannelHandler(private val client: ClientInterface) : ChannelInboundHandlerAdapter() {

    override fun channelActive(ctx: ChannelHandlerContext) {
        val session = client.createSession()
        session.socket = NettySessionSocket(ctx.channel())

        client.onConnectionEstablished(session)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        try {
            val packet = NettyPacket.fromByteArray(msg as ByteArray)

            client.onPacketReceived(packet)
        } catch (exception: Exception) {
            exception.printStackTrace()

            if (ctx.channel().isOpen) {
                ctx.channel().close()
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}
