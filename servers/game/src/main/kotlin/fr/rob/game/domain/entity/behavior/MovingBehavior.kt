package fr.rob.game.domain.entity.behavior

import fr.rob.game.domain.entity.Unit

class MovingBehavior(private val unit: Unit) : BehaviorInterface {
    override fun update(deltaTime: Int) {
        val movement = unit.currentMovement!!
        val traveledDistance: Float = movement.speed * (deltaTime.toFloat() / 1000)

        unit.moveTo(unit.position.x + traveledDistance, unit.position.y, unit.position.z)
    }

    override fun isActive(): Boolean = unit.isMoving()
}
