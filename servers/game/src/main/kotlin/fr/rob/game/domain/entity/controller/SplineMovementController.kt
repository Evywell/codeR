package fr.rob.game.domain.entity.controller

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.movement.Movable
import fr.rob.game.domain.entity.movement.spline.SplineMovementBrainInterface

class SplineMovementController(
    private val source: WorldObject,
    private val splineMovementBrain: SplineMovementBrainInterface
) {
    fun initiateMovementToPosition(destination: Position) {
        if (source.getTrait<Movable>().isEmpty) {
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

            val movableTrait = source.getTrait<Movable>()

            movableTrait.get().moveToPosition(splineMovement.position!!, splineMovement.movement!!)

            if (splineMovement.hasReachDestination) {
                splineMovement.closeSplineMovement()
            }
        }
    }
}
