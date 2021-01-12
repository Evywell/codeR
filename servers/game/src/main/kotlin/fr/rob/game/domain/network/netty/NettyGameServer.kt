package fr.rob.game.domain.network.netty

import fr.rob.game.SSL_ENABLED
import fr.rob.game.domain.log.LoggerFactoryInterface
import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.domain.network.GameServer
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import kotlin.concurrent.thread

class NettyGameServer(private val port: Int, name: String, loggerFactory: LoggerFactoryInterface) : GameServer(name, loggerFactory) {

    private val bootstrap: ServerBootstrap = ServerBootstrap()

    override fun start() {
        val loopGroup: EventLoopGroup = NioEventLoopGroup()
        val workerGroup: EventLoopGroup = NioEventLoopGroup()

        bootstrap.group(loopGroup, workerGroup).channel(NioServerSocketChannel::class.java)
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        // Our handler
        bootstrap.childHandler(NettyServerInitializer(this, SSL_ENABLED))

        val channel: ChannelFuture = bootstrap.bind(port).sync()

        thread(start = true) {
            channel.channel().closeFuture().sync()
        }
    }

}
