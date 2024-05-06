package fr.rob.game.infra.network.physic.unity

import fr.raven.proto.message.physicbridge.PhysicProto.Packet
import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.core.opcode.v2.OpcodeHandler
import fr.rob.game.infra.network.physic.PhysicFunctionParameters
import fr.rob.game.infra.network.physic.PhysicOpcodeFunctionRegistry
import java.util.concurrent.ConcurrentLinkedQueue

class UnityPhysicClient(opcodeFunctionRegistry: PhysicOpcodeFunctionRegistry) : AbstractClient<Packet>() {
    private val awaitingPacketsToBeFlushed = ConcurrentLinkedQueue<Any>()
    private val opcodeHandler = OpcodeHandler(opcodeFunctionRegistry)

    override fun onConnectionEstablished(session: Session) {
        this.session = session

        while (awaitingPacketsToBeFlushed.isNotEmpty()) {
            send(awaitingPacketsToBeFlushed.poll())
        }
    }

    override fun send(message: Any) {
        if (!isSessionInitialized()) {
            awaitingPacketsToBeFlushed.add(message)

            return
        }

        super.send(message)
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)

    override fun onPacketReceived(packet: Packet) {
        opcodeHandler.process(packet.opcode, PhysicFunctionParameters(packet.opcode, packet, session))
    }
}
