package fr.rob.game.domain.spell

import fr.rob.game.domain.entity.UpdatableTraitInterface
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.spell.target.SpellTargetParameter

class SpellCasterTrait(private val caster: WorldObject, private val spellBook: SpellBook) : UpdatableTraitInterface {
    fun castSpell(spellId: Int, target: SpellTargetParameter) {
        val spellInfoQuery = spellBook.getSpellInfo(spellId)

        if (spellInfoQuery.isEmpty) {
            // INVALID SPELL ID, NOT IN THE SPELL BOOK
            return
        }

        spellBook.castSpell(Spell(spellInfoQuery.get(), caster, target))
    }

    override fun update(deltaTime: Int) {
        spellBook.update(deltaTime)
    }
}
