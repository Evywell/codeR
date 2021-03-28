package fr.rob.login.network.netty

import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.network.netty.NettyServer
import fr.rob.core.network.netty.NettyServerHandler
import fr.rob.login.LoginApplication
import fr.rob.login.opcode.LoginOpcodeHandler

class NettyLoginServer(app: LoginApplication, loggerFactory: LoggerFactoryInterface, port: Int, ssl: Boolean) :
    NettyServer(port, ssl) {

    val opcodeHandler = LoginOpcodeHandler(app.env, app.processManager, loggerFactory.create("opcode"))

    init {
        opcodeHandler.initialize()
    }

    override fun handler(): NettyServerHandler = NettyLoginServerHandler(this)
}