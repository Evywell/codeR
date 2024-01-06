package fr.rob.game.domain.entity.behavior

import fr.rob.game.domain.combat.DamageSource
import fr.rob.game.domain.combat.HealthUpdatedEvent
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.world.RollEngineInterface
import kotlin.math.max

class ObjectSheetTrait(
    private val owner: WorldObject,
    private val baseHealth: Int,
    private val rollEngine: RollEngineInterface,
) {
    var health = baseHealth
        private set

    private var baseCriticalChance = DEFAULT_BASE_CRITICAL_CHANCE
    private var criticalChanceReduction = DEFAULT_CRITICAL_CHANCE_REDUCTION

    fun applyDamages(damageSources: List<DamageSource>, isCritical: Boolean) {
        // @todo tmp values
        val criticalMultiplier = if (isCritical) 2 else 1

        var damageTaken = 0
        damageSources.forEach { damageSource -> damageTaken += damageSource.amount * criticalMultiplier }

        if (health - damageTaken < 0) {
            health = 0
        } else {
            health -= damageTaken
        }

        owner.pushEvent(HealthUpdatedEvent(owner, health))
    }

    fun isCriticalHit(caster: WorldObject): Boolean {
        // Here we use integer percentages where 100 = 1% and 1 = 0.01%
        // It avoids to work with float values
        // We roll 0-100 because we have at least 1% chance to do a critical strike
        val roll = rollEngine.roll(0, MAX_ROLL_FOR_PERCENTAGE)

        val casterObjectSheetTrait = caster.getTrait<ObjectSheetTrait>()
        val baseCriticalChance = if (casterObjectSheetTrait.isPresent) {
            casterObjectSheetTrait.get().baseCriticalChance
        } else {
            DEFAULT_BASE_CRITICAL_CHANCE
        }

        val ownerObjectSheetTrait = owner.getTrait<ObjectSheetTrait>()
        val criticalChanceReduction = if (ownerObjectSheetTrait.isPresent) {
            ownerObjectSheetTrait.get().criticalChanceReduction
        } else {
            DEFAULT_CRITICAL_CHANCE_REDUCTION
        }

        val criticalChance = max(DEFAULT_BASE_CRITICAL_CHANCE, baseCriticalChance - criticalChanceReduction)

        return roll <= criticalChance
    }

    companion object {
        const val DEFAULT_BASE_CRITICAL_CHANCE = 100 // 1%
        const val DEFAULT_CRITICAL_CHANCE_REDUCTION = 0

        const val MAX_ROLL_FOR_PERCENTAGE = 10000
    }
}
