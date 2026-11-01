package fr.rob.game.physic.unity

import fr.raven.proto.message.physicbridge.PhysicProto.Packet
import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.core.opcode.v2.OpcodeHandler
import fr.rob.game.physic.PhysicFunctionParameters
import fr.rob.game.physic.PhysicOpcodeFunctionRegistry

/**
 * Receiving side of the physic connection. Every method runs on the Netty event loop of the connection.
 * Packets are sent through [UnityIntegration].
 */
class UnityPhysicClient(opcodeFunctionRegistry: PhysicOpcodeFunctionRegistry) : AbstractClient<Packet>() {
    private val opcodeHandler = OpcodeHandler(opcodeFunctionRegistry)

    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)

    override fun onPacketReceived(packet: Packet) {
        opcodeHandler.process(packet.opcode, PhysicFunctionParameters(packet.opcode, packet, session))
    }
}
