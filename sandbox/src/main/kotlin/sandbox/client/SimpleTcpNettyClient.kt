package sandbox.client

import fr.rob.core.network.v2.ClientProcessInterface
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress

class SimpleTcpNettyClient(private val hostname: String, private val port: Int) : ClientProcessInterface {
    override fun start() {
        val group: EventLoopGroup = NioEventLoopGroup()

        try {
            val clientBootstrap = Bootstrap()

            clientBootstrap.group(group)
            clientBootstrap.channel(NioSocketChannel::class.java)
            clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true)
            clientBootstrap.remoteAddress(InetSocketAddress(hostname, port))

            clientBootstrap.handler(SimpleTcpNettyInitializer())

            clientBootstrap.connect().sync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
