package fr.rob.core.network.netty

import fr.rob.core.network.session.Session
import fr.rob.core.network.Packet
import io.netty.channel.Channel
import io.netty.util.NetUtil
import java.net.InetSocketAddress

open class NettySession(private val channel: Channel) : Session() {

    override fun getIp(): String {
        val address = channel.remoteAddress() as InetSocketAddress

        return NetUtil.bytesToIpAddress(address.address.address)
    }

    override fun send(packet: Packet) {
        channel.writeAndFlush(packet.toByteArray())
    }

    override fun close() {
        channel.close()
    }
}
