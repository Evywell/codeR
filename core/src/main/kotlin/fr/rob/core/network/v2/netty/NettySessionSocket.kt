package fr.rob.core.network.v2.netty

import fr.rob.core.network.Packet
import fr.rob.core.network.session.SessionSocketInterface
import io.netty.channel.Channel
import io.netty.util.NetUtil
import java.net.InetSocketAddress

open class NettySessionSocket(private val channel: Channel) : SessionSocketInterface {

    override fun getIp(): String {
        val remoteAddress = channel.remoteAddress() as InetSocketAddress

        return NetUtil.bytesToIpAddress(remoteAddress.address.address) // address.address.address.address.address
    }

    override fun send(packet: Packet) {
        channel.writeAndFlush(packet.toBytes())
    }

    override fun close() {
        channel.close()
    }
}
