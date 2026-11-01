package fr.rob.game.physic

import com.google.protobuf.Message
import fr.raven.proto.message.physicbridge.PhysicProto
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectMoved
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.world.WorldTaskQueue

class ObjectMovedHandler(
    private val physicObjectInteraction: PhysicObjectInteraction,
    private val worldTaskQueue: WorldTaskQueue,
) : PhysicOpcodeFunction() {
    override fun createMessageFromPacket(packet: PhysicProto.Packet): Message = ObjectMoved.parseFrom(packet.body)

    override fun callForMessage(message: Message, functionParameters: PhysicFunctionParameters) {
        message as ObjectMoved

        worldTaskQueue.enqueue { physicObjectInteraction.invokeObjectInteractionCallback(ObjectGuid(message.guid), message) }
    }

    override fun isCallAuthorized(functionParameters: PhysicFunctionParameters): Boolean = true
}
