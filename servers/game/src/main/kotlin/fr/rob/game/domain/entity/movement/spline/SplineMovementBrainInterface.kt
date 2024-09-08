package fr.rob.game.domain.entity.movement.spline

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject

interface SplineMovementBrainInterface {
    fun moveToDestination(source: WorldObject, destination: Position, stepHandler: (SplineMovement) -> Unit)
}
