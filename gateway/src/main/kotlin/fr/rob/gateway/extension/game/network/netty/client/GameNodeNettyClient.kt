package fr.rob.gateway.extension.game.network.netty.client

import fr.rob.core.network.v2.ClientInterface
import fr.rob.core.network.v2.ClientProcessInterface
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress
import fr.raven.proto.message.game.GameProto.Packet as GamePacket

class GameNodeNettyClient(private val hostname: String, private val port: Int, private val client: ClientInterface<GamePacket>) : ClientProcessInterface {
    override fun start() {
        val group: EventLoopGroup = NioEventLoopGroup()

        try {
            val clientBootstrap = Bootstrap()

            clientBootstrap.group(group)
            clientBootstrap.channel(NioSocketChannel::class.java)
            clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true)
            clientBootstrap.remoteAddress(InetSocketAddress(hostname, port))
            clientBootstrap.handler(GameNodeNettyChannelInitializer(client))

            clientBootstrap.connect().sync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
