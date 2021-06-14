package fr.rob.client.network

import fr.rob.core.log.LoggerFactory
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress

class Client(private val hostname: String, private val port: Int) {

    val logger = LoggerFactory.create("client")
    lateinit var session: Session
    lateinit var clientHandler: ClientHandler

    fun open() {
        Thread(ClientProcess(this)).start()
    }

    fun send(packet: Packet) {
        session.send(packet)
    }

    class ClientProcess(private val client: Client) : Runnable {
        override fun run() {
            val group: EventLoopGroup = NioEventLoopGroup()

            try {
                val clientBootstrap = Bootstrap()

                clientBootstrap.group(group)
                clientBootstrap.channel(NioSocketChannel::class.java)
                clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                clientBootstrap.remoteAddress(InetSocketAddress(client.hostname, client.port))
                clientBootstrap.handler(ClientChannelInitializer(client))

                val channelFuture: ChannelFuture = clientBootstrap.connect().sync()

                channelFuture.channel().closeFuture().sync()
            } finally {
                group.shutdownGracefully().sync()
            }
        }
    }
}
