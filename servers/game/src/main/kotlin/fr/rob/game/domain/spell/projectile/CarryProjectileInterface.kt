package fr.rob.game.domain.spell.projectile

interface CarryProjectileInterface {
    fun update(deltaTime: Int)
    fun hasHitTarget(): Boolean
}
