package fr.rob.game.entity.movement

import fr.rob.game.entity.Position
import fr.rob.game.entity.WorldObject
import fr.rob.game.map.maths.Vector3f

class Movable(private val source: WorldObject) {
    var currentMovement = Movement(Vector3f.zero(), Phase.STOPPED)
        private set

    fun moveToPosition(position: Position, movement: Movement) {
        source.setPosition(position.x, position.y, position.z, position.orientation)
        currentMovement = movement
    }

    fun isMoving() = currentMovement.isMoving()

    data class Movement(val direction: Vector3f, val phase: Phase) {
        fun isMoving(): Boolean = phase == Phase.MOVING
    }

    enum class Phase {
        MOVING, STOPPED
    }
}
