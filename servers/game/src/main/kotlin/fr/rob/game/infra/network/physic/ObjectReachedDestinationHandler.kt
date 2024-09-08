package fr.rob.game.infra.network.physic

import com.google.protobuf.Message
import fr.raven.proto.message.physicbridge.PhysicProto
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectReachDestination
import fr.rob.game.domain.entity.guid.ObjectGuid

class ObjectReachedDestinationHandler(
    private val physicObjectInteraction: PhysicObjectInteraction,
) : PhysicOpcodeFunction() {
    override fun createMessageFromPacket(packet: PhysicProto.Packet): Message = ObjectReachDestination.parseFrom(packet.body)

    override fun callForMessage(message: Message, functionParameters: PhysicFunctionParameters) {
        message as ObjectReachDestination

        physicObjectInteraction.invokeObjectInteractionCallback(ObjectGuid(message.guid), message)
    }

    override fun isCallAuthorized(functionParameters: PhysicFunctionParameters): Boolean = true
}
