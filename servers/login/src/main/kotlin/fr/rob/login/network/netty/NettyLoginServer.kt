package fr.rob.login.network.netty

import fr.rob.core.event.EventManagerInterface
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.network.netty.NettyServer
import fr.rob.core.network.netty.NettyServerHandler
import fr.rob.core.network.netty.plugin.security.RequestLimiter
import fr.rob.core.network.session.Session
import fr.rob.core.security.SecurityBanProcess
import fr.rob.login.LoginApplication
import fr.rob.login.network.LoginSession
import fr.rob.login.opcode.LoginOpcodeHandler

class NettyLoginServer(
    app: LoginApplication,
    eventManager: EventManagerInterface,
    securityBanProcess: SecurityBanProcess?,
    loggerFactory: LoggerFactoryInterface,
    port: Int,
    ssl: Boolean
) :
    NettyServer(port, ssl, eventManager, securityBanProcess, loggerFactory.create("login")) {

    init {
        registerPlugin(RequestLimiter())
    }

    val opcodeHandler = LoginOpcodeHandler(app.env, app.processManager, eventManager, loggerFactory.create("opcode"))

    override fun start() {
        super.start()

        opcodeHandler.initialize()
    }

    override fun handler(): NettyServerHandler = NettyLoginServerHandler(this)

    override fun createSession(): Session = LoginSession()
}
