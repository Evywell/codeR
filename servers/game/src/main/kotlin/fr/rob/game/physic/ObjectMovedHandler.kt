package fr.rob.game.physic

import com.google.protobuf.Message
import fr.raven.proto.message.physicbridge.PhysicProto
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectMoved
import fr.rob.game.entity.guid.ObjectGuid

class ObjectMovedHandler(
    private val physicObjectInteraction: PhysicObjectInteraction,
) : PhysicOpcodeFunction() {
    override fun createMessageFromPacket(packet: PhysicProto.Packet): Message = ObjectMoved.parseFrom(packet.body)

    override fun callForMessage(message: Message, functionParameters: PhysicFunctionParameters) {
        message as ObjectMoved

        physicObjectInteraction.invokeObjectInteractionCallback(ObjectGuid(message.guid), message)
    }

    override fun isCallAuthorized(functionParameters: PhysicFunctionParameters): Boolean = true
}
