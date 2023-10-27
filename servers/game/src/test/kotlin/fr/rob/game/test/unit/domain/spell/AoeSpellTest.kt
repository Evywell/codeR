package fr.rob.game.test.unit.domain.spell

import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.behavior.ObjectSheetTrait
import fr.rob.game.domain.spell.SpellBook
import fr.rob.game.domain.spell.SpellCasterTrait
import fr.rob.game.domain.spell.SpellInfo
import fr.rob.game.domain.spell.effect.InstantAoeDamageEffect
import fr.rob.game.domain.spell.effect.SummonInstantDamageAoeEffect
import fr.rob.game.domain.spell.target.SpellTargetParameter
import fr.rob.game.test.unit.tools.VoidEventDispatcher
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

    @Test
    fun `when summoning an AOE, enemies in it should take damage`() {
        val eventDispatcher = VoidEventDispatcher()

        caster.getTrait<SpellCasterTrait>().get().castSpell(2, SpellTargetParameter(null, caster.mapInstance))

        // First frame, targets should be untouched
        assertEquals(100, caster.getTrait(ObjectSheetTrait::class).get().health)
        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)

        // Let's jump after the first tick
        instance.update(500, eventDispatcher)

        assertEquals(90, caster.getTrait(ObjectSheetTrait::class).get().health)
        assertEquals(90, target.getTrait(ObjectSheetTrait::class).get().health)

        // Let's jump 200 ms => the second tick is not ready yet, no changes
        instance.update(200, eventDispatcher)

        assertEquals(90, caster.getTrait(ObjectSheetTrait::class).get().health)
        assertEquals(90, target.getTrait(ObjectSheetTrait::class).get().health)

        // Let's jump 400 ms => the second tick is past since 100ms
        instance.update(400, eventDispatcher)

        assertEquals(80, caster.getTrait(ObjectSheetTrait::class).get().health)
        assertEquals(80, target.getTrait(ObjectSheetTrait::class).get().health)

        // Let's jump 400 ms => the script should be ending
        instance.update(400, eventDispatcher)

        assertEquals(80, caster.getTrait(ObjectSheetTrait::class).get().health)
        assertEquals(80, target.getTrait(ObjectSheetTrait::class).get().health)
    }

    override fun createSpellBook(): SpellBook = SpellBook(
        hashMapOf(
            1 to SpellInfo(
                1,
                SpellInfo.LaunchType.INSTANT,
                arrayOf(InstantAoeDamageEffect.InstantAoeDamageEffectInfo(10, 5f)),
            ),
            2 to SpellInfo(
                2,
                SpellInfo.LaunchType.INSTANT,
                arrayOf(SummonInstantDamageAoeEffect.SummonInstantDamageAoeEffectInfo(10, 5f, 500, 1500)),
            ),
        ),
    )
}
