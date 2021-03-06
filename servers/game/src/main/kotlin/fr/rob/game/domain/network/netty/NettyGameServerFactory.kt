package fr.rob.game.domain.network.netty

import fr.rob.core.BaseApplication
import fr.rob.core.log.LoggerFactory
import fr.rob.core.network.Server
import fr.rob.core.network.ServerFactoryInterface
import fr.rob.core.process.ProcessManager
import fr.rob.core.security.SecurityBanProcess

class NettyGameServerFactory(
    private val app: BaseApplication,
    private val processManager: ProcessManager
) : ServerFactoryInterface {

    override fun build(port: Int, name: String, ssl: Boolean): Server =
        NettyGameServer(
            name,
            port,
            ssl,
            processManager,
            app,
            processManager.getOrMakeProcess(SecurityBanProcess::class),
            LoggerFactory
        )
}
