package fr.rob.gateway.extension.eas

import com.google.protobuf.InvalidProtocolBufferException
import fr.raven.proto.message.eas.EasProto.EasPacket
import fr.raven.proto.message.gateway.GatewayProto
import fr.rob.gateway.network.GatewaySession
import fr.rob.gateway.network.dispatcher.PacketDispatcherInterface
import fr.rob.gateway.network.exception.InvalidPacketStructure

class EasPacketDispatcher(private val easService: EasService) : PacketDispatcherInterface {
    override fun support(packet: GatewayProto.Packet, session: GatewaySession): Boolean =
        GatewayProto.Packet.Context.EAS == packet.context

    override fun dispatch(packet: GatewayProto.Packet, session: GatewaySession) {
        try {
            val easPacket = EasPacket.parseFrom(packet.body)

            easService.authenticate(session, easPacket)
        } catch (_: InvalidProtocolBufferException) {
            throw InvalidPacketStructure()
        }
    }

    override fun transmitInterruption(session: GatewaySession) {}
}
