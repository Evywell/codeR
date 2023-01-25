package fr.rob.game.domain.entity.behavior

interface BehaviorInterface {
    fun update(deltaTime: Int)
    fun isActive(): Boolean
}
