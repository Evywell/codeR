package fr.rob.game.domain.network.netty

import fr.rob.core.BaseApplication
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.network.netty.NettyServer
import fr.rob.core.network.netty.NettyServerHandler
import fr.rob.core.network.session.Session
import fr.rob.core.process.ProcessManager
import fr.rob.core.security.SecurityBanProcess
import fr.rob.game.domain.network.session.GameSession
import fr.rob.game.domain.network.session.SessionPoolManager

class NettyGameServer(
    val name: String,
    port: Int,
    ssl: Boolean,
    val processManager: ProcessManager,
    val app: BaseApplication,
    securityBanProcess: SecurityBanProcess,
    val loggerFactory: LoggerFactoryInterface
) :
    NettyServer(port, ssl, securityBanProcess, loggerFactory.create(name)) {

    private val sessionPoolManager = SessionPoolManager()

    override fun handler(): NettyServerHandler = NettyGameServerHandler(this)

    override fun createSession(): Session = GameSession()

    override fun registerSession(identifier: Int, session: Session) {
        super.registerSession(identifier, session)

        sessionPoolManager.addSession(session)
    }
}
