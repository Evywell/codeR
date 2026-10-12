package fr.rob.game.entity.movement.spline

import fr.raven.log.LoggerInterface
import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.entity.movement.spline.exception.ActionNotHandledByBrainException

class SplineWithFallbackMovementBrain(
    private val wrappedBrains: Array<SplineMovementBrainInterface>,
    private val logger: LoggerInterface
) : SplineMovementBrainInterface {
    override fun moveToDestination(
        source: WorldObject,
        destination: Position,
        stepHandler: (SplineMovement) -> Unit
    ) {
        if (wrappedBrains.isEmpty()) {
            throw IllegalArgumentException("No wrapped brains provided")
        }

        for (brain in wrappedBrains) {
            try {
                brain.moveToDestination(source, destination, stepHandler)
                return
            } catch (e: ActionNotHandledByBrainException) {
                logger.warning("Brain ${brain::class.simpleName} did not handle the action: ${e.message}")
            }
        }

        throw IllegalStateException("No brain was able to handle the movement of ${source.guid.getRawValue()}")
    }
}