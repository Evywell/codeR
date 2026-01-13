package fr.rob.game.ability.projectile

interface CarryProjectileInterface {
    fun update(deltaTime: Int)
    fun hasHitTarget(): Boolean
}
