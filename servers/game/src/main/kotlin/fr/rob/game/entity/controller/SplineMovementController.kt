package fr.rob.game.entity.controller

import fr.rob.game.behavior.MovableBehavior
import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.movement.spline.SplineMovementBrainInterface

class SplineMovementController(
    private val source: WorldObject,
    private val splineMovementBrain: SplineMovementBrainInterface
) {
    fun initiateMovementToPosition(destination: Position) {
        if (!source.hasComponent(MovementComponent::class)) {
            throw Exception("Trying to initiate a spline movement on non movable object")
        }

        splineMovementBrain.moveToDestination(source, destination) { splineMovement ->
            if (!splineMovement.isInProgress()) {
                return@moveToDestination
            }

            if (
                source.position.isSameHas(splineMovement.position!!)
                && !splineMovement.hasReachDestination
            ) {
                return@moveToDestination
            }

            val movableBehavior = source.getBehavior<MovableBehavior>()
            movableBehavior?.moveObjectToPosition(source, splineMovement.position!!, splineMovement.movement!!)

            if (splineMovement.hasReachDestination) {
                splineMovement.closeSplineMovement()
            }
        }
    }
}
