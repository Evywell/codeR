package fr.rob.game.spell

import java.util.Optional

class SpellBook(private val availableSpells: HashMap<Int, SpellInfo>) {
    fun getSpellInfo(spellId: Int): Optional<SpellInfo> = Optional.ofNullable(availableSpells[spellId])
}
