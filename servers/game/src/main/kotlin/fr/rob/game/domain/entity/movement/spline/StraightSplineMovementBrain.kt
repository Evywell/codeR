package fr.rob.game.domain.entity.movement.spline

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.movement.Movable
import fr.rob.game.domain.maths.Vector3f

/**
 * For the moment this is pretty dumb, it teleports the object instead of moving it gradually
 */
class StraightSplineMovementBrain : SplineMovementBrainInterface {
    override fun moveToDestination(source: WorldObject, destination: Position, stepHandler: (SplineMovement) -> Unit) {
        val movement = SplineMovement()

        movement.position = destination
        movement.movement = Movable.Movement(Vector3f.forward(), Movable.Phase.MOVING)

        stepHandler(movement)
    }
}
