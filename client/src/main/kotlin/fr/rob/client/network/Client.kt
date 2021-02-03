package fr.rob.client.network

import fr.rob.game.domain.network.packet.Packet
import fr.rob.game.domain.network.session.Session
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture

import java.net.InetSocketAddress

import io.netty.channel.socket.nio.NioSocketChannel

import io.netty.channel.nio.NioEventLoopGroup

import io.netty.channel.EventLoopGroup


class Client(private val hostname: String, private val port: Int) {

    lateinit var session: Session

    fun open() {
        val group: EventLoopGroup = NioEventLoopGroup()
        try {
            val clientBootstrap = Bootstrap()

            clientBootstrap.group(group)
            clientBootstrap.channel(NioSocketChannel::class.java)
            clientBootstrap.remoteAddress(InetSocketAddress(hostname, port))
            clientBootstrap.handler(ClientChannelInitializer(this))
            val channelFuture: ChannelFuture = clientBootstrap.connect().sync()

            channelFuture.channel().closeFuture().sync()
        } finally {
            group.shutdownGracefully().sync()
        }
    }

    fun send(packet: Packet) {
        session.send(packet)
    }
}
