package fr.rob.game.entity.movement.spline

import fr.raven.proto.message.physicbridge.PhysicProto
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectMoveTo
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectMoved
import fr.raven.proto.message.physicbridge.PhysicProto.ObjectReachDestination
import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.map.grid.query.predicate.CanSeeObject
import fr.rob.game.map.grid.query.predicate.IsAPlayer
import fr.rob.game.map.maths.Vector3f
import fr.rob.game.physic.PhysicObjectInteraction
import fr.rob.game.physic.unity.UnityIntegration
import fr.rob.game.player.message.ObjectMovingToDestinationMessage
import fr.rob.game.world.WorldTaskQueue

/**
 * Moves objects with the physic server. Every method and callback runs on the world thread,
 * except the connection lost listener, which only hands over through [WorldTaskQueue].
 */
class UnitySplineMovementBrain(
    private val physicClientIntegration: UnityIntegration,
    private val physicObjectInteraction: PhysicObjectInteraction,
    worldTaskQueue: WorldTaskQueue,
) : SplineMovementBrainInterface {
    private val activeSplines = HashMap<ObjectGuid, ActiveSpline>()

    init {
        physicClientIntegration.addConnectionLostListener {
            worldTaskQueue.enqueue { stopActiveSplines() }
        }
    }

    override fun moveToDestination(source: WorldObject, destination: Position, stepHandler: (SplineStep) -> Unit) {
        // A new spline replaces the previous one of the same object
        finish(source.guid)

        physicClientIntegration.send(
            PhysicProto.Packet.newBuilder()
                .setOpcode(MOVE_REQUEST_OPCODE)
                .setBody(
                    PhysicProto.MoveRequest.newBuilder()
                        .setGuid(source.guid.getRawValue())
                        .setOrigin(source.position.toPhysicPosition())
                        .setDestination(destination.toPhysicPosition())
                        .build()
                        .toByteString(),
                )
                .build(),
        )

        activeSplines[source.guid] = ActiveSpline(source, stepHandler)

        physicObjectInteraction.registerObjectInteractionCallback(source.guid, ObjectMoved::class) {
            it as ObjectMoved

            stepHandler(
                SplineStep(
                    Position(it.position.posX, it.position.posY, it.position.posZ, it.position.orientation),
                    MovementComponent.MovementInfo(Vector3f.fromVec3Message(it.direction), MovementComponent.Phase.MOVING),
                    reachedDestination = false,
                ),
            )
        }

        physicObjectInteraction.registerObjectInteractionCallback(source.guid, ObjectReachDestination::class) {
            finish(source.guid)
            stepHandler(stoppedStep(source))
        }

        physicObjectInteraction.registerObjectInteractionCallback(source.guid, ObjectMoveTo::class) {
            it as ObjectMoveTo

            notifyMovingToDestination(source, Vector3f.fromVec3Message(it.destination))
        }
    }

    private fun stopActiveSplines() {
        val stopped = activeSplines.values.toList()

        stopped.forEach { spline ->
            finish(spline.source.guid)
            spline.stepHandler(stoppedStep(spline.source))
        }
    }

    private fun finish(guid: ObjectGuid) {
        activeSplines.remove(guid)
        physicObjectInteraction.unregisterObjectInteractionCallbacks(guid)
    }

    private fun stoppedStep(source: WorldObject) = SplineStep(
        source.position,
        MovementComponent.MovementInfo(Vector3f.forward(), MovementComponent.Phase.STOPPED),
        reachedDestination = true,
    )

    private fun notifyMovingToDestination(source: WorldObject, destination: Vector3f) {
        source.mapInstance.grid
            .query()
            .getObjects(source.getCell(), arrayOf(IsAPlayer(), CanSeeObject(source)))
            .forEach { visibleObject ->
                visibleObject.controlledByGameSession?.send(ObjectMovingToDestinationMessage(source.guid, destination))
            }
    }

    private fun Position.toPhysicPosition(): PhysicProto.Position =
        PhysicProto.Position.newBuilder()
            .setPosX(x)
            .setPosY(y)
            .setPosZ(z)
            .setOrientation(orientation)
            .build()

    private class ActiveSpline(val source: WorldObject, val stepHandler: (SplineStep) -> Unit)

    companion object {
        const val MOVE_REQUEST_OPCODE = 1
    }
}
