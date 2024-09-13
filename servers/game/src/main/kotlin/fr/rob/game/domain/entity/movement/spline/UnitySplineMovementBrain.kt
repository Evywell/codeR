package fr.rob.game.domain.entity.movement.spline

import fr.raven.proto.message.physicbridge.PhysicProto
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectMoved
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectReachDestination
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.movement.Movable
import fr.rob.game.domain.maths.Vector3f
import fr.rob.game.domain.world.DelayedUpdateQueue
import fr.rob.game.infra.network.physic.PhysicObjectInteraction
import fr.rob.game.infra.network.physic.SplineStepDelayedUpdate
import fr.rob.game.infra.network.physic.unity.UnityIntegration

class UnitySplineMovementBrain(
    private val physicClientIntegration: UnityIntegration,
    private val physicObjectInteraction: PhysicObjectInteraction,
    private val delayedUpdateQueue: DelayedUpdateQueue,
) : SplineMovementBrainInterface {
    override fun moveToDestination(source: WorldObject, destination: Position, stepHandler: (SplineMovement) -> Unit) {
        val sourceGuidRawValue = source.guid.getRawValue()

        physicClientIntegration.send(
            PhysicProto.Packet.newBuilder()
                .setOpcode(1)
                .setBody(
                    PhysicProto.SpawnRequest.newBuilder()
                        .setGuid(sourceGuidRawValue)
                        .setPosition(
                            PhysicProto.Position.newBuilder()
                                .setPosX(source.position.x)
                                .setPosY(source.position.y)
                                .setPosZ(source.position.z)
                                .setOrientation(source.position.orientation),
                        )
                        .build()
                        .toByteString(),
                )
                .build(),
        )

        val splineMovement = SplineMovement()

        physicObjectInteraction.registerObjectInteractionCallback(source.guid, ObjectMoved::class) {
            it as ObjectMoved

            splineMovement.position = Position(it.position.posX, it.position.posY, it.position.posZ, it.position.orientation)
            splineMovement.movement = Movable.Movement(Vector3f.fromVec3Message(it.direction), Movable.Phase.MOVING)

            delayedUpdateQueue.enqueueDelayedUpdate(SplineStepDelayedUpdate(stepHandler, splineMovement))
        }

        physicObjectInteraction.registerObjectInteractionCallback(source.guid, ObjectReachDestination::class) {
            it as ObjectReachDestination

            splineMovement.reachDestination()
            splineMovement.movement = Movable.Movement(Vector3f.forward(), Movable.Phase.STOPPED)

            delayedUpdateQueue.enqueueDelayedUpdate(SplineStepDelayedUpdate(stepHandler, splineMovement))
        }

        physicClientIntegration.send(
            PhysicProto.Packet.newBuilder()
                .setOpcode(2)
                .setBody(
                    PhysicProto.MoveToRequest.newBuilder()
                        .setGuid(sourceGuidRawValue)
                        .setPosition(
                            PhysicProto.Position.newBuilder()
                                .setPosX(destination.x)
                                .setPosY(destination.y)
                                .setPosZ(destination.z)
                                .setOrientation(destination.orientation),
                        )
                        .build()
                        .toByteString(),
                )
                .build(),
        )
    }
}
