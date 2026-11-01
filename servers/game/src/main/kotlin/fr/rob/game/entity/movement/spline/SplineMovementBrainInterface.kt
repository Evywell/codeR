package fr.rob.game.entity.movement.spline

import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject

interface SplineMovementBrainInterface {
    /**
     * Called on the world thread. [stepHandler] is always called on the world thread.
     *
     * @throws fr.rob.game.entity.movement.spline.exception.ActionNotHandledByBrainException when the brain cannot move the object
     */
    fun moveToDestination(source: WorldObject, destination: Position, stepHandler: (SplineStep) -> Unit)
}
