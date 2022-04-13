package fr.rob.core.network.netty.event

import fr.rob.core.event.Event
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.session.Session

data class NettyChannelReadEvent(val opcode: Int, val session: Session, val packet: Packet) :
    Event() {

    var shouldProcessPacket = true

    override fun getName(): String = NETTY_CHANNEL_READ

    companion object {
        const val NETTY_CHANNEL_READ = "NettyChannelReadEvent"
    }
}
