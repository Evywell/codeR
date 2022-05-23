package fr.rob.core.network.v2.netty.builder

import fr.rob.core.network.v2.netty.NettyBufferedSessionSocket
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.core.network.v2.session.SessionSocketUpdater
import io.netty.channel.ChannelHandlerContext

class NettyBufferedSocketBuilder(private val updater: SessionSocketUpdater) : NettySessionSocketBuilderInterface {
    override fun build(channelContext: ChannelHandlerContext): SessionSocketInterface {
        val socket = NettyBufferedSessionSocket(channelContext.channel())
        updater.addSocket(socket)

        return socket
    }
}
