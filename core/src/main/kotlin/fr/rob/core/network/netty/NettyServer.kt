package fr.rob.core.network.netty

import fr.rob.core.network.Server
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import kotlin.concurrent.thread

abstract class NettyServer(
    private val port: Int,
    private val ssl: Boolean
) : Server() {

    private val bootstrap: ServerBootstrap = ServerBootstrap()

    override fun start() {
        val loopGroup: EventLoopGroup = NioEventLoopGroup()
        val workerGroup: EventLoopGroup = NioEventLoopGroup()

        bootstrap.group(loopGroup, workerGroup).channel(NioServerSocketChannel::class.java)
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        // Our handler
        bootstrap.childHandler(NettyServerInitializer(this, ssl))

        val channel: ChannelFuture = bootstrap.bind(port).sync()

        thread(start = true) {
            channel.channel().closeFuture().sync()
        }
    }

    abstract fun handler(): NettyServerHandler
}
