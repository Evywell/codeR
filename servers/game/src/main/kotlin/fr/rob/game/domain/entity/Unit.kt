package fr.rob.game.domain.entity

import fr.rob.game.domain.entity.behavior.MovingBehavior
import fr.rob.game.domain.entity.guid.ObjectGuid

open class Unit(
    guid: ObjectGuid,
    val name: String,
    var level: Int
) : WorldObject(guid) {
    var currentMovement: Movement? = null
        private set

    fun addToWorld(objectManager: ObjectManager) {
        objectManager.addToGrid(this)
        this.isInWorld = true
    }

    fun setMoving(behavior: MovingBehavior, movement: Movement) {
        currentMovement = movement
        behaviors.add(behavior)
    }

    fun isMoving(): Boolean = currentMovement != null

    fun moveTo(x: Float, y: Float, z: Float) {
        position.x = x
        position.y = y
        position.z = z

        // println("Moved to $position")
    }
}
