package fr.rob.game.domain.spell

import java.util.Optional

class SpellBook(private val availableSpells: HashMap<Int, SpellInfo>) {
    private val ongoingSpells = ArrayList<Spell>()

    fun getSpellInfo(spellId: Int): Optional<SpellInfo> = Optional.ofNullable(availableSpells[spellId])

    fun castSpell(spell: Spell) {
        ongoingSpells.add(spell)
        spell.cast()
    }

    fun update(deltaTime: Int) {
        ongoingSpells.forEach { spell -> spell.update(deltaTime) }
        ongoingSpells.removeIf { spell -> spell.isEnded() }
    }
}
