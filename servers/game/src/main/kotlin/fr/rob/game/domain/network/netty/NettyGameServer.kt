package fr.rob.game.domain.network.netty

import fr.rob.core.BaseApplication
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.netty.NettyServer
import fr.rob.core.network.netty.NettyServerHandler
import fr.rob.core.process.ProcessManager

class NettyGameServer(
    val name: String,
    port: Int,
    ssl: Boolean,
    val processManager: ProcessManager,
    val app: BaseApplication,
    val loggerFactory: LoggerFactoryInterface
) :
    NettyServer(port, ssl) {

    val logger: LoggerInterface = loggerFactory.create(name)

    override fun handler(): NettyServerHandler = NettyGameServerHandler(this)
}