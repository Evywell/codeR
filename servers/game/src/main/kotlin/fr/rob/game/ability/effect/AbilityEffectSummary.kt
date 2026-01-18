package fr.rob.game.ability.effect

import fr.rob.game.combat.DamageSource
import fr.rob.game.entity.WorldObject

class AbilityEffectSummary {
    private val targetDamageSources = HashMap<WorldObject, ArrayList<DamageSource>>()

    fun addDamageSourceForTarget(target: WorldObject, damageSource: DamageSource) {
        if (!targetDamageSources.containsKey(target)) {
            targetDamageSources[target] = ArrayList()
        }

        targetDamageSources[target]!!.add(damageSource)
    }

    fun getDamageSources(): Map<WorldObject, List<DamageSource>> = targetDamageSources

    fun hasDamages(): Boolean = targetDamageSources.isNotEmpty()
}