package fr.rob.core.network.v2.netty

import fr.rob.core.network.v2.ServerInterface
import fr.rob.core.network.v2.ServerProcessInterface
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

class NettyServer(private val port: Int, private val server: ServerInterface, private val ssl: Boolean) :
    ServerProcessInterface {

    private val bootstrap: ServerBootstrap = ServerBootstrap()
    private lateinit var channelFuture: ChannelFuture

    override fun start() {
        val loopGroup: EventLoopGroup = NioEventLoopGroup()
        val workerGroup: EventLoopGroup = NioEventLoopGroup()

        bootstrap.group(loopGroup, workerGroup).channel(NioServerSocketChannel::class.java)
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        // Our handler
        bootstrap.childHandler(NettyChannelInitializer(server, ssl))

        // Wait for the server to launch
        channelFuture = bootstrap.bind(port).sync()
    }
}
