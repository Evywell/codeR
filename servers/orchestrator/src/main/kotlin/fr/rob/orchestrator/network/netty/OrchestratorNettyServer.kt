package fr.rob.orchestrator.network.netty

import fr.rob.core.event.EventManagerInterface
import fr.rob.core.log.LoggerFactoryInterface
import fr.rob.core.network.netty.NettyServer
import fr.rob.core.network.netty.NettyServerHandler
import fr.rob.core.network.session.Session
import fr.rob.core.process.ProcessManager
import fr.rob.core.security.SecurityBanProcess
import fr.rob.orchestrator.network.OrchestratorSession
import fr.rob.orchestrator.opcode.OrchestratorOpcodeHandler

open class OrchestratorNettyServer(
    port: Int,
    ssl: Boolean,
    eventManager: EventManagerInterface,
    securityBanProcess: SecurityBanProcess?,
    loggerFactory: LoggerFactoryInterface,
    processManager: ProcessManager
) : NettyServer(port, ssl, eventManager, securityBanProcess, loggerFactory.create("orchestrator server")) {

    val opcodeHandler = OrchestratorOpcodeHandler(processManager, loggerFactory.create("orchestrator opcode"))

    override fun start() {
        super.start()

        opcodeHandler.initialize()
    }

    override fun handler(): NettyServerHandler = OrchestratorServerHandler(this)

    override fun createSession(): Session = OrchestratorSession()
}
