package fr.rob.game.infra.network.physic

import com.google.protobuf.Message
import fr.raven.proto.message.physicbridge.PhysicProto
import fr.rob.game.domain.entity.guid.ObjectGuid

class ObjectMovingWithDestinationHandler(private val physicObjectInteraction: PhysicObjectInteraction) : PhysicOpcodeFunction() {
    override fun createMessageFromPacket(packet: PhysicProto.Packet): Message = PhysicProto.ObjectMoveTo.parseFrom(packet.body)

    override fun callForMessage(message: Message, functionParameters: PhysicFunctionParameters) {
        message as PhysicProto.ObjectMoveTo

        physicObjectInteraction.invokeObjectInteractionCallback(ObjectGuid(message.guid), message)
    }

    override fun isCallAuthorized(functionParameters: PhysicFunctionParameters): Boolean = true
}
