package fr.rob.game.test.unit.domain.spell

import fr.rob.game.domain.entity.behavior.HealthResourceTrait
import fr.rob.game.domain.spell.SpellBook
import fr.rob.game.domain.spell.SpellCasterTrait
import fr.rob.game.domain.spell.SpellInfo
import fr.rob.game.domain.spell.effect.InstantDamageEffect
import fr.rob.game.domain.spell.target.SpellTargetParameter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SpellBookTest : SpellCasterEnvironmentBaseTest() {
    @Test
    fun `Ended spell should not be updated anymore`() {
        // Arrange & Act
        caster.getTrait(SpellCasterTrait::class).get().castSpell(1, SpellTargetParameter(target.guid, caster.mapInstance))
        spellBook.update(100)

        // Assert
        Assertions.assertEquals(99, target.getTrait(HealthResourceTrait::class).get().health)
    }

    override fun createSpellBook(): SpellBook = SpellBook(
        hashMapOf(
            1 to SpellInfo(SpellInfo.LaunchType.INSTANT, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(1))),
        ),
    )
}
