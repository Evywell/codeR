package fr.rob.game.domain.entity.movement.spline

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.movement.Movable

open class SplineMovement {
    var position: Position? = null
    var movement: Movable.Movement? = null

    private var isDone = false

    fun isInProgress(): Boolean = isStarted() && !isDone
    private fun isStarted(): Boolean = position != null
}
