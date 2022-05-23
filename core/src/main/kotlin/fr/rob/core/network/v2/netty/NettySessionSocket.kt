package fr.rob.core.network.v2.netty

import io.netty.channel.Channel

open class NettySessionSocket(channel: Channel) : AbstractNettySessionSocket(channel) {
    override fun send(data: Any) {
        channel.writeAndFlush(data)
    }
}
