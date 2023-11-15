package fr.rob.game.test.unit.domain.spell

import fr.rob.game.domain.entity.Movement
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.behavior.MovableTrait
import fr.rob.game.domain.entity.behavior.ObjectSheetTrait
import fr.rob.game.domain.spell.Spell
import fr.rob.game.domain.spell.SpellBook
import fr.rob.game.domain.spell.SpellCasterTrait
import fr.rob.game.domain.spell.SpellEffectInfo
import fr.rob.game.domain.spell.SpellEffectSummary
import fr.rob.game.domain.spell.SpellEffectTypeEnum
import fr.rob.game.domain.spell.SpellInfo
import fr.rob.game.domain.spell.SpellInfo.Companion.CASTING_TIME_2_SECONDS
import fr.rob.game.domain.spell.effect.EffectFromSpellInterface
import fr.rob.game.domain.spell.effect.EffectInterface
import fr.rob.game.domain.spell.effect.InstantDamageEffect
import fr.rob.game.domain.spell.target.SpellTargetParameter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.EnumSet
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpellCasterTraitTest : SpellCasterEnvironmentBaseTest() {
    @Test
    fun `I should be able to cast instant spells`() {
        caster.getTrait(SpellCasterTrait::class).get().castSpell(1, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(97, target.getTrait(ObjectSheetTrait::class).get().health)
    }

    @Test
    fun `I should be able to cast spell with projectiles`() {
        target.position.x = 6f

        caster.getTrait(SpellCasterTrait::class).get().castSpell(2, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)

        // Projectile still moving
        caster.onUpdate(1000)
        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)

        // Projectile hit the target
        caster.onUpdate(1000)
        assertEquals(99, target.getTrait(ObjectSheetTrait::class).get().health)
    }

    @Test
    fun `I should be able to cast spell with timed projectiles`() {
        target.position.x = 6f

        caster.getTrait(SpellCasterTrait::class).get().castSpell(3, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)

        // Projectile still moving
        caster.onUpdate(1000)
        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)

        // Projectile hit the target
        caster.onUpdate(1000)
        assertEquals(99, target.getTrait(ObjectSheetTrait::class).get().health)
    }

    @Test
    fun `I should be able to cast spell with casting time`() {
        caster.getTrait<SpellCasterTrait>().get().castSpell(4, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)
        // 1 second casting
        caster.onUpdate(1000)
        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)
        // 2 seconds casting
        caster.onUpdate(1000)
        assertEquals(94, target.getTrait(ObjectSheetTrait::class).get().health)
    }

    @Test
    fun `The cast time should break when moving`() {
        caster.addTrait(MovableTrait(caster))
        caster.getTrait<SpellCasterTrait>().get().castSpell(4, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)
        // 1 second casting
        caster.onUpdate(1000)
        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)

        // Walking
        caster.getTrait<MovableTrait>().get().move(Movement(Movement.MovementDirectionType.FORWARD, 0f))
        caster.onUpdate(100)
        assertEquals(Spell.SpellState.CANCELED, caster.getTrait<SpellCasterTrait>().get().getLastSpellCasted().get().state)
        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)

        // 2 more seconds casting
        caster.onUpdate(2000)
        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)
    }

    @Test
    fun `The cast time should not break when moving if has flag ALLOW_CAST_WHILE_MOVING`() {
        caster.addTrait(MovableTrait(caster))
        caster.getTrait<SpellCasterTrait>().get().castSpell(6, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)
        // 1 second casting
        caster.onUpdate(1000)
        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)

        // Walking
        caster.getTrait<MovableTrait>().get().move(Movement(Movement.MovementDirectionType.FORWARD, 0f))
        caster.onUpdate(100)
        assertEquals(Spell.SpellState.PREPARING, caster.getTrait<SpellCasterTrait>().get().getLastSpellCasted().get().state)
        assertEquals(100, target.getTrait(ObjectSheetTrait::class).get().health)

        // 2 more seconds casting
        caster.onUpdate(2000)
        assertEquals(99, target.getTrait(ObjectSheetTrait::class).get().health)
    }

    @Test
    fun `Ended spell should not be updated anymore`() {
        val counterTrait = CounterTrait()
        caster.addTrait(counterTrait)
        caster.getTrait(SpellCasterTrait::class).get().castSpell(5, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(1, counterTrait.counter)
        caster.onUpdate(100)
        assertEquals(1, counterTrait.counter)
    }

    private class CounterEffect(private val caster: WorldObject) : EffectInterface {
        class CounterEffectInfo : SpellEffectInfo(SpellEffectTypeEnum.INSTANT_DAMAGE), EffectFromSpellInterface {
            override fun createEffectFromSpell(spell: Spell): EffectInterface = CounterEffect(spell.caster)
        }

        override fun cast(spellEffectSummary: SpellEffectSummary) {
            caster.getTrait<CounterTrait>().get().increment()
        }
    }

    private class CounterTrait {
        var counter = 0
            private set

        fun increment() {
            counter++
        }
    }

    @ParameterizedTest
    @MethodSource("critDamageDataProvider")
    fun `When the hit is a crit, I should have correct damage amount`(rollResult: Int, healthRemaining: Int) {
        // Arrange
        targetRiggedDiceEngine.nextRollResult = rollResult

        // Act
        caster.getTrait(SpellCasterTrait::class).get().castSpell(1, SpellTargetParameter(target.guid, caster.mapInstance))

        // Assert
        assertEquals(healthRemaining, target.getTrait(ObjectSheetTrait::class).get().health)
    }

    fun critDamageDataProvider(): Stream<Arguments> = Stream.of(
        // We rolled a 1 (out of 100) -> it's a crit !
        // 3 (base damage) * 2 (crit !) = 6 => 100 - 6 = 94
        Arguments.of(100, 94),
        // We rolled a 1.01 (out of 100) -> it is not a crit
        Arguments.of(101, 97),
        // We rolled a 0.9 (out of 100) -> it's a crit
        Arguments.of(90, 94),
        // We rolled a 0 (out of 100) -> it's a crit
        Arguments.of(0, 94),
        // We rolled a 100 (out of 100) -> it is not a crit
        Arguments.of(10000, 97),
    )

    override fun createSpellBook(): SpellBook =
        SpellBook(
            hashMapOf(
                1 to SpellInfo(1, SpellInfo.LaunchType.INSTANT, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(3))),
                2 to SpellInfo(2, SpellInfo.LaunchType.GHOST_PROJECTILES, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(1)), projectileSpeed = 3f),
                3 to SpellInfo(3, SpellInfo.LaunchType.TIMED_PROJECTILES, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(1)), projectileSpeed = 3f),
                4 to SpellInfo(4, SpellInfo.LaunchType.INSTANT, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(6)), CASTING_TIME_2_SECONDS),
                5 to SpellInfo(5, SpellInfo.LaunchType.INSTANT, arrayOf(CounterEffect.CounterEffectInfo())),
                6 to SpellInfo(6, SpellInfo.LaunchType.INSTANT, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(1)), CASTING_TIME_2_SECONDS, flags = EnumSet.of(SpellInfo.FLAGS.ALLOW_CAST_WHILE_MOVING)),
            ),
        )
}
