package sandbox.client

import com.google.protobuf.Message
import fr.raven.log.LoggerInterface
import fr.raven.proto.message.eas.EasProto
import fr.raven.proto.message.game.NearbyObjectOpcodeProto
import fr.raven.proto.message.gateway.GatewayProto.Packet
import fr.rob.core.network.v2.AbstractClient
import fr.rob.core.network.v2.session.Session
import fr.rob.core.network.v2.session.SessionSocketInterface
import fr.rob.gateway.extension.eas.EAS_AUTHENTICATION_RESULT
import java.util.concurrent.ConcurrentLinkedQueue
import fr.raven.proto.message.gateway.GatewayProto.Packet.Context.EAS as EASContext
import fr.raven.proto.message.gateway.GatewayProto.Packet.Context.GAME as GameContext

class GatewayClient(private val logger: LoggerInterface) : AbstractClient<Packet>() {
    val packetStack = ConcurrentLinkedQueue<Packet>()

    private val packetMessageMapping: Map<MessageMappingKey, Message> = mapOf(
        MessageMappingKey(0x03, GameContext) to NearbyObjectOpcodeProto.NearbyObjectOpcode.getDefaultInstance(),
        MessageMappingKey(EAS_AUTHENTICATION_RESULT, EASContext) to EasProto.EasAuthenticationResult.getDefaultInstance()
    )

    override fun onConnectionEstablished(session: Session) {
        this.session = session
    }

    override fun onPacketReceived(packet: Packet) {
        debugPacket(packet)
        packetStack.add(packet)
    }

    override fun createSession(socket: SessionSocketInterface): Session = Session(socket)

    private fun debugPacket(packet: Packet) {
        val messageBuilder = packetMessageMapping[MessageMappingKey(packet.opcode, packet.context)]

        val description = debugMessage(messageBuilder, packet)

        logger.debug("[${packet.context}] Packed received with opcode ${packet.opcode} $description")
    }

    private fun debugMessage(messageType: Message?, packet: Packet): String {
        if (messageType == null) {
            return ""
        }

        val message = messageType.parserForType.parseFrom(packet.body)
        val fields = ArrayList<String>()
        val description = StringBuilder(message::class.qualifiedName + " { ")

        message.allFields.forEach { (key, value) ->
            fields.add(key.name)
            description.append("${key.name} = $value, ")
        }

        message.descriptorForType.fields.forEach { field ->
            val fieldName = field.name

            if (!fields.contains(fieldName)) {
                description.append("$fieldName = ${field.defaultValue}, ")
            }
        }

        description.append("} ")

        return description.toString()
    }

    private data class MessageMappingKey(val opcode: Int, val context: Packet.Context)
}
