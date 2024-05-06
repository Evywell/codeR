package fr.rob.game.domain.movement

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.WorldObject

class Movable(private val source: WorldObject) {
    var currentMovement = Movement(DirectionType.NONE, Phase.STOPPED)
        private set

    fun moveToPosition(position: Position, movement: Movement) {
        source.setPosition(position.x, position.y, position.z, position.orientation)
        currentMovement = movement
    }

    fun isMoving() = currentMovement.isMoving()

    data class Movement(val direction: DirectionType, val phase: Phase) {
        fun isMoving(): Boolean = phase == Phase.MOVING
    }

    enum class DirectionType {
        FORWARD, BACKWARD, LEFT, RIGHT, STRAFE_LEFT, STRAFE_RIGHT, NONE
    }

    enum class Phase {
        MOVING, STOPPED
    }
}
