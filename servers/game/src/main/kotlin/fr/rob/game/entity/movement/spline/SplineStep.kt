package fr.rob.game.entity.movement.spline

import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.Position

/**
 * One immutable step of a spline movement, handed to the step handler on the world thread.
 */
data class SplineStep(
    val position: Position,
    val movement: MovementComponent.MovementInfo,
    val reachedDestination: Boolean,
)
