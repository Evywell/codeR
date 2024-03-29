package fr.rob.core.service.network

import fr.raven.log.NullLogger
import fr.rob.core.network.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.core.opcode.OpcodeHandler
import fr.rob.core.service.Action
import fr.rob.core.service.opcode.ServiceOpcodeHandler

class ServiceServer(private val actions: Collection<Action>) : Server<Packet>() {
    override fun onPacketReceived(session: Session, packet: Packet) {
        session as ServiceSession

        session.handler.process(packet.readOpcode(), session, packet)
    }

    override fun createSession(socket: SessionSocketInterface): Session {
        val handler = createHandler()
        val session = ServiceSession(handler, socket)

        handler.initialize()

        return session
    }

    private fun createHandler(): OpcodeHandler = ServiceOpcodeHandler(actions, NullLogger())
}
