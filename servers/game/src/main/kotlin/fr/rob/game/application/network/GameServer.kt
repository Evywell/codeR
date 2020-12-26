package fr.rob.game.application.network

import fr.rob.game.SSL_ENABLED
import fr.rob.game.domain.log.LoggerInterface
import fr.rob.game.domain.server.GameServer
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import kotlin.concurrent.thread

class GameServer(private val port: Int, logger: LoggerInterface) : GameServer(logger) {

    private val bootstrap: ServerBootstrap = ServerBootstrap()

    fun start() {
        val loopGroup: EventLoopGroup = NioEventLoopGroup()
        val workerGroup: EventLoopGroup = NioEventLoopGroup()

        bootstrap.group(loopGroup, workerGroup).channel(NioServerSocketChannel::class.java)
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)

        // Our handler
        bootstrap.childHandler(ServerInitializer(this, SSL_ENABLED))

        val channel: ChannelFuture = bootstrap.bind(port).sync()

        thread(start = true) {
            channel.channel().closeFuture().sync()
        }
    }

}