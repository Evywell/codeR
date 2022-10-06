package sandbox.client

import fr.raven.log.LoggerInterface
import fr.raven.proto.message.gateway.GatewayProto
import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import java.util.concurrent.ConcurrentLinkedQueue

class GatewayClient(private val logger: LoggerInterface) : AbstractClient<GatewayProto.Packet>() {
    val packetStack = ConcurrentLinkedQueue<GatewayProto.Packet>()

    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun onPacketReceived(packet: GatewayProto.Packet) {
        logger.debug("[${packet.context}] Packed received with opcode ${packet.opcode}")
        packetStack.add(packet)
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)
}
