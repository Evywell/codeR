package fr.rob.game.physic

import com.google.protobuf.Message
import fr.raven.proto.message.physicbridge.PhysicProto
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.world.WorldTaskQueue

class ObjectMovingWithDestinationHandler(
    private val physicObjectInteraction: PhysicObjectInteraction,
    private val worldTaskQueue: WorldTaskQueue,
) : PhysicOpcodeFunction() {
    override fun createMessageFromPacket(packet: PhysicProto.Packet): Message = PhysicProto.ObjectMoveTo.parseFrom(packet.body)

    override fun callForMessage(message: Message, functionParameters: PhysicFunctionParameters) {
        message as PhysicProto.ObjectMoveTo

        worldTaskQueue.enqueue { physicObjectInteraction.invokeObjectInteractionCallback(ObjectGuid(message.guid), message) }
    }

    override fun isCallAuthorized(functionParameters: PhysicFunctionParameters): Boolean = true
}
