package fr.rob.game.entity.movement.spline

import fr.rob.game.component.MovementComponent
import fr.rob.game.entity.Position

open class SplineMovement {
    var position: Position? = null
    var movement: MovementComponent.MovementInfo? = null
    var hasReachDestination = false
        private set

    private var isDone = false

    fun reachDestination() {
        hasReachDestination = true
    }

    fun closeSplineMovement() {
        isDone = true
    }

    fun isInProgress(): Boolean = isStarted() && !isDone
    private fun isStarted(): Boolean = position != null
}
