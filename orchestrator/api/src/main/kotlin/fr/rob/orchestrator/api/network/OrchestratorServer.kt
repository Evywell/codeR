package fr.rob.orchestrator.api.network

import fr.raven.log.LoggerInterface
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.core.process.ProcessManager
import fr.rob.orchestrator.api.opcode.OrchestratorApiOpcodeHandler
import fr.rob.orchestrator.shared.Orchestrator

class OrchestratorServer(
    private val orchestrator: Orchestrator,
    private val logger: LoggerInterface,
    private val processManager: ProcessManager
) : Server<Packet>() {
    override fun onPacketReceived(session: Session, packet: Packet) {
        session as OrchestratorSession

        val opcode = packet.readOpcode()
        session.handler.process(opcode, session, packet)
    }

    override fun createSession(socket: SessionSocketInterface): Session {
        val handler = OrchestratorApiOpcodeHandler(orchestrator, logger, processManager)
        handler.initialize()

        return OrchestratorSession(handler, socket)
    }
}
