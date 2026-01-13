package fr.rob.game.ability.effect

import fr.rob.game.ability.Ability
import fr.rob.game.combat.DamageSource
import fr.rob.game.entity.WorldObject

class InstantAoeDamageEffect(
    private val effectInfo: InstantAoeDamageEffectInfo,
    private val caster: WorldObject,
) : EffectInterface {
    override fun apply(effectSummary: AbilityEffectSummary) {
        val origin = caster.position
        val objects = caster.mapInstance.findObjectsInsideRadius(origin, effectInfo.radius)

        objects.forEach { worldObject ->
            if (worldObject != caster) {
                effectSummary.addDamageSourceForTarget(worldObject, DamageSource(caster, effectInfo.damageValue))
            }
        }
    }

    class InstantAoeDamageEffectInfo(val damageValue: Int, val radius: Float) : AbilityEffectInfoInterface {
        override fun createEffectFromAbility(abilityEffectInfo: Ability): EffectInterface =
            InstantAoeDamageEffect(this, abilityEffectInfo.source)
    }
}
