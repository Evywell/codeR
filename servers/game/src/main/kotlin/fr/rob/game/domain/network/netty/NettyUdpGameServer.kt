package fr.rob.game.domain.network.netty

import fr.rob.core.BaseApplication
import fr.rob.game.domain.log.LoggerFactoryInterface
import fr.rob.game.domain.network.GameServer
import fr.rob.game.domain.process.ProcessManager
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import java.net.InetAddress

class NettyUdpGameServer(
    private val port: Int,
    name: String,
    loggerFactory: LoggerFactoryInterface,
    app: BaseApplication,
    processManager: ProcessManager
) : GameServer(name, loggerFactory, app, processManager) {

    override fun start() {
        val group = NioEventLoopGroup()
        val b = Bootstrap()
        b.group(group).channel(NioDatagramChannel::class.java)
            .option(ChannelOption.SO_BROADCAST, true)
            .option(ChannelOption.SO_BACKLOG, 128)
            .handler(NettyUpdServerInitializer(this))

        val address = InetAddress.getLocalHost()

        b.bind(address, port).sync().channel().closeFuture().await()
    }
}
