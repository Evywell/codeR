package fr.rob.gateway.network.netty.client

import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.ClientProcessInterface
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress

class NettyClient(private val hostname: String, private val port: Int, private val client: ClientInterface<Packet>) :
    ClientProcessInterface {

    lateinit var group: EventLoopGroup

    override fun start() {
        group = NioEventLoopGroup()

        try {
            val clientBootstrap = Bootstrap()

            clientBootstrap.group(group)
            clientBootstrap.channel(NioSocketChannel::class.java)
            clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true)
            clientBootstrap.remoteAddress(InetSocketAddress(hostname, port))
            clientBootstrap.handler(NettyChannelInitializer(client))

            clientBootstrap.connect().sync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        group.shutdownGracefully()
    }
}
