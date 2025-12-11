package fr.rob.game.spell.effect

import fr.rob.game.combat.DamageSource
import fr.rob.game.entity.WorldObject
import fr.rob.game.spell.Spell
import fr.rob.game.spell.SpellEffectInfo
import fr.rob.game.spell.SpellEffectSummary
import fr.rob.game.spell.SpellEffectTypeEnum

class InstantAoeDamageEffect(
    private val spellEffectInfo: InstantAoeDamageEffectInfo,
    private val caster: WorldObject,
) : EffectInterface {
    override fun cast(spellEffectSummary: SpellEffectSummary) {
        // Select the enemies inside the AOE
        val origin = caster.position
        val objects = caster.mapInstance.findObjectsInsideRadius(origin, spellEffectInfo.radius)

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
