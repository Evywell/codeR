package fr.rob.core.network.v2.netty

import io.netty.channel.Channel
import java.util.concurrent.ConcurrentLinkedQueue

class NettyBufferedSessionSocket(channel: Channel) : AbstractNettySessionSocket(channel) {
    private val packetBuffer = ConcurrentLinkedQueue<Any>()

    override fun send(data: Any) {
        packetBuffer.add(data)
    }

    fun update(): Boolean {
        var packetQueued = 0

        while (packetBuffer.isNotEmpty()) {
            channel.write(packetBuffer.poll())
            packetQueued++

            if (shouldFlushChannelBuffer(packetQueued)) {
                channel.flush()
                packetQueued = 0
            }
        }

        return true
    }

    private fun shouldFlushChannelBuffer(packetQueued: Int): Boolean =
        packetBuffer.isEmpty() ||
            isPacketQueuedThresholdExceeded(packetQueued)

    private fun isPacketQueuedThresholdExceeded(packetQueued: Int): Boolean = packetQueued > MAX_PACKET_SENT_PER_UPDATE

    companion object {
        const val MAX_PACKET_SENT_PER_UPDATE = 10
    }
}
