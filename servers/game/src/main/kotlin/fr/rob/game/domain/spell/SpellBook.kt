package fr.rob.game.domain.spell

import java.util.Optional

class SpellBook(private val availableSpells: HashMap<Int, SpellInfo>) {
    fun getSpellInfo(spellId: Int): Optional<SpellInfo> = Optional.ofNullable(availableSpells[spellId])
}
