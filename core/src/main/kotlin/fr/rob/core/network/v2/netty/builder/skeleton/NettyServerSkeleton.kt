package fr.rob.core.network.v2.netty.builder.skeleton

import fr.rob.core.network.v2.ServerProcessInterface
import fr.rob.core.network.v2.netty.builder.shard.ConfigShardInterface
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup

class NettyServerSkeleton<T>(
    private val configShard: ConfigShardInterface,
    private val channelInitializerSkeleton: ChannelInitializerSkeleton<T>,
    private val port: Int
) : ServerProcessInterface {
    private val bootstrap: ServerBootstrap = ServerBootstrap()
    private lateinit var channelFuture: ChannelFuture

    override fun start() {
        val loopGroup: EventLoopGroup = configShard.getLoopGroup()
        val workerGroup: EventLoopGroup = configShard.getWorkerGroup()

        bootstrap.group(loopGroup, workerGroup).channel(configShard.getServerSocketClass())
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        // Our handler
        bootstrap.childHandler(channelInitializerSkeleton)

        // Wait for the server to launch
        channelFuture = bootstrap.bind(port).sync()
    }
}
