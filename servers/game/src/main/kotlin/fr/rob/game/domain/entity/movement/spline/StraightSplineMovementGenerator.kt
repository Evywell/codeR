package fr.rob.game.domain.entity.movement.spline

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.movement.Movable

/**
 * For the moment this is pretty dumb, it teleports the object instead of moving it gradually
 */
class StraightSplineMovementGenerator : SplineMovementGeneratorInterface {
    override fun generateForFinalPosition(source: WorldObject, position: Position): SplineMovement {
        val movement = SplineMovement()

        movement.position = position
        movement.movement = Movable.Movement(Movable.DirectionType.FORWARD, Movable.Phase.MOVING)

        return movement
    }
}
