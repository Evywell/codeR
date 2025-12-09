package fr.rob.core.network.v2.netty

import fr.rob.core.network.v2.ServerProcessInterface
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.MultiThreadIoEventLoopGroup
import io.netty.channel.nio.NioIoHandler
import io.netty.channel.socket.nio.NioServerSocketChannel

abstract class NettyServer<T>(
    protected val port: Int,
    protected val ssl: Boolean
) : ServerProcessInterface {

    private val bootstrap: ServerBootstrap = ServerBootstrap()
    private lateinit var channelFuture: ChannelFuture

    override fun start() {
        val loopGroup: EventLoopGroup = MultiThreadIoEventLoopGroup(NioIoHandler.newFactory())
        val workerGroup: EventLoopGroup = MultiThreadIoEventLoopGroup(NioIoHandler.newFactory())

        bootstrap.group(loopGroup, workerGroup).channel(NioServerSocketChannel::class.java)
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        // Our handler
        bootstrap.childHandler(channelInitializer())

        // Wait for the server to launch
        channelFuture = bootstrap.bind(port).sync()
    }

    protected abstract fun channelInitializer(): NettyChannelInitializer<T>
}
