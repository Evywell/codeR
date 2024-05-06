package fr.rob.game.domain.entity.movement.spline

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject

interface SplineMovementGeneratorInterface {
    fun generateForFinalPosition(source: WorldObject, position: Position): SplineMovement
}
