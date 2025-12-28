package fr.rob.game.spell

import fr.rob.game.entity.UpdatableTraitInterface
import fr.rob.game.entity.WorldObject
import fr.rob.game.spell.target.SpellTargetParameter
import java.util.Optional

@Deprecated("Use Ability system instead")
class SpellCasterTrait(private val caster: WorldObject, private val spellBook: SpellBook) : UpdatableTraitInterface {
    private val ongoingSpells = ArrayList<Spell>()
    private val spellHistory = ArrayList<Spell>()

    fun castSpell(spellId: Int, target: SpellTargetParameter) {
        val spellInfoQuery = spellBook.getSpellInfo(spellId)

        if (spellInfoQuery.isEmpty) {
            // INVALID SPELL ID, NOT IN THE SPELL BOOK
            return
        }

        val spell = Spell(spellInfoQuery.get(), caster, target)

        ongoingSpells.add(spell)
        spellHistory.add(spell)

        spell.cast()
    }

    fun getLastSpellCasted(): Optional<Spell> = Optional.ofNullable(spellHistory.lastOrNull())

    override fun update(deltaTime: Int) {
        ongoingSpells.forEach { spell -> spell.update(deltaTime) }
        ongoingSpells.removeIf { spell -> spell.isEnded() }
    }
}
