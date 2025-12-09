package fr.rob.core.network.v2.netty.client

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.ClientProcessInterface
import fr.rob.core.network.v2.netty.basic.client.BasicNettyChannelInitializer
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.nio.NioIoHandler
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress

class NettyClient(private val hostname: String, private val port: Int, private val client: ClientInterface<Packet>) :
    ClientProcessInterface {
    override fun start() {
        val group: EventLoopGroup = MultiThreadIoEventLoopGroup(NioIoHandler.newFactory())

        try {
            val clientBootstrap = Bootstrap()

            clientBootstrap.group(group)
            clientBootstrap.channel(NioSocketChannel::class.java)
            clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true)
            clientBootstrap.remoteAddress(InetSocketAddress(hostname, port))
            clientBootstrap.handler(BasicNettyChannelInitializer(client))

            clientBootstrap.connect().sync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
