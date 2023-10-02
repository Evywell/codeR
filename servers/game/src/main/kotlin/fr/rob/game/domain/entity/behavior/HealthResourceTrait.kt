package fr.rob.game.domain.entity.behavior

import fr.rob.game.domain.combat.DamageSource

class HealthResourceTrait(private val baseHealth: Int) {
    var health = baseHealth
        private set

    fun applyDamages(damageSources: List<DamageSource>) {
        damageSources.forEach { damageSource -> health -= damageSource.amount }
    }
}
