package fr.rob.game.infra.network.physic.unity

import fr.rob.core.network.v2.ClientProcessInterface
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress

class UnityTcpClient(private val hostname: String, private val port: Int, private val client: UnityPhysicClient) : ClientProcessInterface {
    override fun start() {
        val group: EventLoopGroup = NioEventLoopGroup()

        try {
            val clientBootstrap = Bootstrap()

            clientBootstrap.group(group)
            clientBootstrap.channel(NioSocketChannel::class.java)
            clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true)
            clientBootstrap.remoteAddress(InetSocketAddress(hostname, port))

            clientBootstrap.handler(UnityTcpInitializer(client))

            clientBootstrap.connect().sync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
