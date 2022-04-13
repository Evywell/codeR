package fr.rob.core.network.v2.netty

import fr.rob.core.network.v2.session.SessionSocketInterface
import io.netty.channel.Channel
import io.netty.util.NetUtil
import java.net.InetSocketAddress

open class NettySessionSocket(protected val channel: Channel) : SessionSocketInterface {
    override fun send(data: Any) {
        channel.writeAndFlush(data)
    }

    override fun getIp(): String {
        val remoteAddress = channel.remoteAddress() as InetSocketAddress

        return NetUtil.bytesToIpAddress(remoteAddress.address.address) // address.address.address.address.address
    }

    override fun close() {
        channel.close()
    }
}
