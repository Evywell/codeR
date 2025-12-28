package fr.rob.game.component

import fr.rob.game.map.maths.Vector3f

class MovementComponent {
    var movementInfo: MovementInfo? = null
        private set

    fun move(movementInfo: MovementInfo) {
        this.movementInfo = movementInfo
    }

    data class MovementInfo(val direction: Vector3f, val phase: Phase) {
        fun isMoving(): Boolean = phase == Phase.MOVING
    }

    fun isMoving(): Boolean = this.movementInfo != null && this.movementInfo!!.phase == Phase.MOVING

    enum class Phase {
        MOVING, STOPPED
    }
}
