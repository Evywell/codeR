package fr.rob.core.network.v2.netty.builder

import fr.rob.core.network.v2.netty.NettySessionSocket
import fr.rob.core.network.v2.session.SessionSocketInterface
import io.netty.channel.ChannelHandlerContext

class NettySessionSocketBuilder : NettySessionSocketBuilderInterface {
    override fun build(channelContext: ChannelHandlerContext): SessionSocketInterface =
        NettySessionSocket(channelContext.channel())
}
