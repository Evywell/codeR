package fr.rob.game.domain.spell.effect

import fr.rob.game.domain.combat.DamageSource
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.spell.Spell

class InstantAoeDamageEffect(
    private val spellEffectInfo: InstantAoeDamageEffectInfo,
    private val caster: WorldObject,
) : EffectInterface {
    override fun cast(spellEffectSummary: SpellEffectSummary) {
        // Select the enemies inside the AOE
        val origin = caster.position
        val objects = caster.mapInstance.grid.findObjectsInsideRadius(origin, spellEffectInfo.radius)

        objects.forEach { worldObject ->
            if (worldObject != caster) {
                spellEffectSummary.addDamageSourceForTarget(worldObject, DamageSource(caster, spellEffectInfo.damageValue))
            }
        }
    }

    class InstantAoeDamageEffectInfo(val damageValue: Int, val radius: Float) : SpellEffectInfo(SpellEffectTypeEnum.INSTANT_DAMAGE), EffectFromSpellInterface {
        override fun createEffectFromSpell(spell: Spell): EffectInterface =
            InstantAoeDamageEffect(this, spell.caster)
    }
}
