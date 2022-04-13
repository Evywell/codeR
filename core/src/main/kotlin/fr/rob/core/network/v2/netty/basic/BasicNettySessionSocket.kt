package fr.rob.core.network.v2.netty.basic

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.netty.NettySessionSocket
import io.netty.channel.Channel

class BasicNettySessionSocket(channel: Channel) : NettySessionSocket(channel) {
    override fun send(data: Any) {
        super.send((data as Packet).toBytes())
    }
}
