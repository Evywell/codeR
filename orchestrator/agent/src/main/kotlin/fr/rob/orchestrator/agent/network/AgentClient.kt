package fr.rob.orchestrator.agent.network

import fr.rob.core.log.LoggerInterface
import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.network.v2.AbstractClient
import fr.rob.orchestrator.agent.node.NodeAgentAdapterInterface

class AgentClient(private val adapter: NodeAgentAdapterInterface, private val logger: LoggerInterface) :
    AbstractClient() {

    override fun onConnectionEstablished(session: Session) {
        this.session = session as AgentSession
    }

    override fun onPacketReceived(packet: Packet) {
        val opcode = packet.readOpcode()

        (this.session as AgentSession).opcodeHandler.process(opcode, this.session, packet)
    }

    override fun createSession(): AgentSession = AgentSession(adapter, responseStack, logger)
}
