package fr.rob.game.test.unit.domain.spell

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.behavior.ObjectSheetTrait
import fr.rob.game.domain.spell.SpellBook
import fr.rob.game.domain.spell.SpellCasterTrait
import fr.rob.game.domain.spell.SpellInfo
import fr.rob.game.domain.spell.effect.InstantAoeDamageEffect
import fr.rob.game.domain.spell.target.SpellTargetParameter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AoeSpellTest : SpellCasterEnvironmentBaseTest() {
    @Test
    fun `enemies inside aoe should be hit and enemies outside should not`() {
        // Arrange
        val outOfRangeTarget1 = createUnit("Out of range target 1", 1, Position(5f, 5f, 0f, 0f))
        val outOfRangeTarget2 = createUnit("Out of range target 2", 1, Position(-5f, -5f, 0f, 0f))
        val inRangeTarget2 = createUnit("Target in range 2", 1, Position(0f, 0f, 0f, 0f))
        val inRangeTarget3 = createUnit("Target in range 3", 1, Position(-3f, -3f, 0f, 0f))

        // Act
        caster.getTrait(SpellCasterTrait::class).get().castSpell(1, SpellTargetParameter(null, caster.mapInstance))

        // Assert
        assertEquals(90, target.getTrait(ObjectSheetTrait::class).get().health)
        assertEquals(90, inRangeTarget2.getTrait(ObjectSheetTrait::class).get().health)
        assertEquals(90, inRangeTarget3.getTrait(ObjectSheetTrait::class).get().health)
        assertEquals(100, outOfRangeTarget1.getTrait(ObjectSheetTrait::class).get().health)
        assertEquals(100, outOfRangeTarget2.getTrait(ObjectSheetTrait::class).get().health)
    }

    override fun createSpellBook(): SpellBook = SpellBook(
        hashMapOf(
            1 to SpellInfo(
                SpellInfo.LaunchType.INSTANT,
                arrayOf(InstantAoeDamageEffect.InstantAoeDamageEffectInfo(10, 5f)),
            ),
        ),
    )
}
