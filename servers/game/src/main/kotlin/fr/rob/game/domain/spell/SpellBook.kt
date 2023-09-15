package fr.rob.game.domain.spell

import fr.rob.game.domain.spell.effect.InstantDamageEffect
import java.util.Optional

class SpellBook {
    private val spells: HashMap<Int, SpellInfo> = hashMapOf(
        1 to SpellInfo(arrayOf(InstantDamageEffect.InstantDamageEffectInfo(3))),
    )

    fun getSpellInfo(spellId: Int): Optional<SpellInfo> = Optional.ofNullable(spells[spellId])
}
