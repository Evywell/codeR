package fr.rob.gateway.extension.realm

import fr.rob.core.network.Packet
import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.core.opcode.v2.OpcodeHandler
import fr.rob.gateway.extension.realm.gamenode.GameNodes
import fr.rob.gateway.extension.realm.opcode.RealmFunctionParameters
import fr.rob.gateway.extension.realm.opcode.RealmOpcodeRegistry

class RealmClient(
    gameNodes: GameNodes,
    realmService: RealmService
) : AbstractClient<Packet>() {
    private val opcodeHandler = OpcodeHandler(
        RealmOpcodeRegistry(gameNodes, realmService)
    )

    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)

    override fun onPacketReceived(packet: Packet) {
        val opcode = packet.readOpcode()
        opcodeHandler.process(opcode, RealmFunctionParameters(opcode, packet))
    }
}
