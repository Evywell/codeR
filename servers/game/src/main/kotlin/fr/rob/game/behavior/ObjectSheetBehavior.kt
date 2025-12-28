package fr.rob.game.behavior

import fr.rob.game.combat.DamageSource
import fr.rob.game.combat.ObjectSheetUpdated
import fr.rob.game.component.attribute.CriticalAttributeComponent
import fr.rob.game.component.resource.HealthComponent
import fr.rob.game.entity.WorldObject
import fr.rob.game.world.RollEngineInterface
import kotlin.collections.forEach
import kotlin.math.max

class ObjectSheetBehavior(private val rollEngine: RollEngineInterface) : BehaviorInterface {
    override fun update(worldObject: WorldObject, deltaTime: Int) {}

    override fun canApplyTo(worldObject: WorldObject): Boolean = true

    fun applyDamages(victim: WorldObject, damageSources: List<DamageSource>, isCritical: Boolean) {
        // @todo tmp values
        val criticalMultiplier = if (isCritical) 2 else 1

        var damageTaken = 0
        damageSources.forEach { damageSource -> damageTaken += damageSource.amount * criticalMultiplier }

        inflictDamages(victim, damageTaken)
    }

    fun applySingleDamage(victim: WorldObject, damageSource: DamageSource, isCritical: Boolean) {
        // @todo tmp values
        val criticalMultiplier = if (isCritical) 2 else 1

        val damageTaken = damageSource.amount * criticalMultiplier

        inflictDamages(victim, damageTaken)
    }

    fun isCriticalHit(caster: WorldObject): Boolean {
        // Here we use integer percentages where 100 = 1% and 1 = 0.01%
        // It avoids to work with float values
        // We roll 0-100 because we have at least 1% chance to do a critical strike
        val roll = rollEngine.roll(0, MAX_ROLL_FOR_PERCENTAGE)

        val baseCriticalChance = caster.getComponent<CriticalAttributeComponent>()?.baseCriticalChance
            ?: CriticalAttributeComponent.DEFAULT_BASE_CRITICAL_CHANCE

        val criticalChanceReduction = caster.getComponent<CriticalAttributeComponent>()?.criticalChanceReduction
            ?: CriticalAttributeComponent.DEFAULT_CRITICAL_CHANCE_REDUCTION

        val criticalChance = max(CriticalAttributeComponent.DEFAULT_CRITICAL_CHANCE_REDUCTION, baseCriticalChance - criticalChanceReduction)

        return roll <= criticalChance
    }

    private fun inflictDamages(victim: WorldObject, damageTaken: Int) {
        victim.getComponent<HealthComponent>()?.let {
            it.reduceHealth(damageTaken)

            victim.pushEvent(ObjectSheetUpdated(victim))
        }
    }

    companion object {
        const val MAX_ROLL_FOR_PERCENTAGE = 10000
    }
}