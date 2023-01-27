package fr.rob.core.network.v2.netty

import io.netty.channel.Channel
import java.util.concurrent.ConcurrentLinkedQueue

class NettyBufferedSessionSocket(channel: Channel) : AbstractNettySessionSocket(channel) {
    private val packetBuffer = ConcurrentLinkedQueue<Any>()

    override fun send(data: Any) {
        packetBuffer.add(data)
    }

    fun update(): Boolean {
        while (packetBuffer.isNotEmpty()) {
            channel.writeAndFlush(packetBuffer.poll())
        }

        return true
    }
}
