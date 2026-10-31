package fr.rob.game.entity.movement

import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject

/**
 * Validates a destination sent by a client before it is applied.
 */
class MovementValidator {
    fun isValidDestination(mover: WorldObject, destination: Position): Boolean =
        destination.orientation.isFinite() && mover.mapInstance.grid.contains(destination)
}
