package fr.rob.game.entity.movement.spline

import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject

interface SplineMovementBrainInterface {
    fun moveToDestination(source: WorldObject, destination: Position, stepHandler: (SplineMovement) -> Unit)
}
