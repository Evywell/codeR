package fr.rob.login.network

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.Server
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.core.opcode.OpcodeHandlerInterface

class LoginServer(private val opcodeHandler: OpcodeHandlerInterface) : Server<Packet>() {
    override fun onPacketReceived(session: Session, packet: Packet) {
        opcodeHandler.process(packet.readOpcode(), session, packet)
    }

    override fun createSession(socket: SessionSocketInterface): Session = LoginSession(socket)
}
