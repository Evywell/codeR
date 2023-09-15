package fr.rob.game.domain.spell

import fr.rob.game.domain.entity.Unit
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.spell.effect.InstantDamageEffect
import fr.rob.game.domain.spell.effect.SpellEffectInfo
import fr.rob.game.domain.spell.effect.SpellEffectTypeEnum
import fr.rob.game.domain.spell.target.SpellTargetParameter

class SpellCasterTrait(private val caster: WorldObject, private val spellBook: SpellBook) {
    fun castSpell(spellId: Int, target: SpellTargetParameter) {
        val spellInfoQuery = spellBook.getSpellInfo(spellId)

        if (spellInfoQuery.isEmpty) {
            // INVALID SPELL ID, NOT IN THE SPELL BOOK
            return
        }

        spellInfoQuery.get().effects.forEach { castEffect(it, target) }
    }

    private fun castEffect(effectInfo: SpellEffectInfo, target: SpellTargetParameter) {
        val effect = when (effectInfo.type) {
            SpellEffectTypeEnum.INSTANT_DAMAGE -> InstantDamageEffect(effectInfo as InstantDamageEffect.InstantDamageEffectInfo, target, (caster as Unit).level)
        }

        effect.cast()
    }
}
