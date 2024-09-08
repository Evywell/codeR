package fr.rob.game.domain.entity.movement.spline

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.movement.Movable

open class SplineMovement {
    var position: Position? = null
    var movement: Movable.Movement? = null
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
