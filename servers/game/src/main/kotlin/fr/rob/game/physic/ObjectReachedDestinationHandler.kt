package fr.rob.game.physic

import com.google.protobuf.Message
import fr.raven.proto.message.physicbridge.PhysicProto
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectReachDestination
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.world.WorldTaskQueue

class ObjectReachedDestinationHandler(
    private val physicObjectInteraction: PhysicObjectInteraction,
    private val worldTaskQueue: WorldTaskQueue,
) : PhysicOpcodeFunction() {
    override fun createMessageFromPacket(packet: PhysicProto.Packet): Message = ObjectReachDestination.parseFrom(packet.body)

    override fun callForMessage(message: Message, functionParameters: PhysicFunctionParameters) {
        message as ObjectReachDestination

        worldTaskQueue.enqueue { physicObjectInteraction.invokeObjectInteractionCallback(ObjectGuid(message.guid), message) }
    }

    override fun isCallAuthorized(functionParameters: PhysicFunctionParameters): Boolean = true
}
