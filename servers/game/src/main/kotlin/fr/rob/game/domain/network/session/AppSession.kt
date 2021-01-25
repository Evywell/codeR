package fr.rob.game.domain.network.session

import fr.rob.game.domain.network.packet.Packet
import io.netty.channel.Channel
import io.netty.util.NetUtil
import java.net.InetSocketAddress

open class AppSession(private val channel: Channel) : Session() {

    override fun getIp(): String {
        val address = channel.remoteAddress() as InetSocketAddress

        return NetUtil.bytesToIpAddress(address.address.address)
    }

    override fun send(packet: Packet) {
        TODO("Not yet implemented: waiting for Routing")
    }

    override fun close() {
        channel.close()
    }
}
