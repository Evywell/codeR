package fr.rob.game.infra.network.physic

import com.google.protobuf.Message
import fr.raven.proto.message.physicbridge.PhysicProto
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectMoved
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.world.DelayedUpdateQueue

class ObjectMovedHandler(
    private val physicObjectInteraction: PhysicObjectInteraction,
    private val delayedUpdateQueue: DelayedUpdateQueue,
) : PhysicOpcodeFunction() {
    override fun createMessageFromPacket(packet: PhysicProto.Packet): Message = ObjectMoved.parseFrom(packet.body)

    override fun callForMessage(message: Message, functionParameters: PhysicFunctionParameters) {
        message as ObjectMoved

        val movement = physicObjectInteraction.movingObjects[message.guid] ?: return

        delayedUpdateQueue.enqueueDelayedUpdate(
            DelayedPhysicUpdate(movement, Position(message.position.posX, message.position.posY, message.position.posZ, message.position.orientation))
        )
    }

    override fun isCallAuthorized(functionParameters: PhysicFunctionParameters): Boolean = true
}
