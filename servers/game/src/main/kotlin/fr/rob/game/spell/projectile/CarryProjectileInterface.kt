package fr.rob.game.spell.projectile

interface CarryProjectileInterface {
    fun update(deltaTime: Int)
    fun hasHitTarget(): Boolean
}
