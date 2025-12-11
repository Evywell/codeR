package fr.rob.game.entity.behavior

interface BehaviorInterface {
    fun update(deltaTime: Int)
    fun isActive(): Boolean
}
