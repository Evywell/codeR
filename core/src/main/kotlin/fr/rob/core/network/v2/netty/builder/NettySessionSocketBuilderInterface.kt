package fr.rob.core.network.v2.netty.builder

import fr.rob.core.network.v2.session.SessionSocketInterface
import io.netty.channel.ChannelHandlerContext

interface NettySessionSocketBuilderInterface {
    fun build(channelContext: ChannelHandlerContext): SessionSocketInterface
}
