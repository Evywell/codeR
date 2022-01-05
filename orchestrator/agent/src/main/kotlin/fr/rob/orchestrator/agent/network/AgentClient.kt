package fr.rob.orchestrator.agent.network

import fr.rob.core.network.Packet
import fr.rob.core.network.session.Session
import fr.rob.core.network.v2.AbstractClient

abstract class AgentClient : AbstractClient() {

    override fun onConnectionEstablished(session: Session) {
        this.session = session as AgentSession
    }

    override fun onPacketReceived(packet: Packet) {
        val opcode = packet.readOpcode()

        (this.session as AgentSession).opcodeHandler.process(opcode, this.session, packet)
    }
}
