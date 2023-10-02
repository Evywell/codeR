package fr.rob.game.test.unit.domain.spell

import fr.rob.game.domain.entity.Movement
import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.entity.behavior.HealthResourceTrait
import fr.rob.game.domain.entity.behavior.MovableTrait
import fr.rob.game.domain.spell.Spell
import fr.rob.game.domain.spell.SpellBook
import fr.rob.game.domain.spell.SpellCasterTrait
import fr.rob.game.domain.spell.SpellInfo
import fr.rob.game.domain.spell.SpellInfo.Companion.CASTING_TIME_2_SECONDS
import fr.rob.game.domain.spell.effect.EffectFromSpellInterface
import fr.rob.game.domain.spell.effect.EffectInterface
import fr.rob.game.domain.spell.effect.InstantDamageEffect
import fr.rob.game.domain.spell.effect.SpellEffectInfo
import fr.rob.game.domain.spell.effect.SpellEffectSummary
import fr.rob.game.domain.spell.effect.SpellEffectTypeEnum
import fr.rob.game.domain.spell.target.SpellTargetParameter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.EnumSet

class SpellCasterTraitTest : SpellCasterEnvironmentBaseTest() {
    @Test
    fun `I should be able to cast instant spells`() {
        caster.getTrait(SpellCasterTrait::class).get().castSpell(1, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(91, target.getTrait(HealthResourceTrait::class).get().health)
    }

    @Test
    fun `I should be able to cast spell with projectiles`() {
        target.position.x = 6f

        caster.getTrait(SpellCasterTrait::class).get().castSpell(2, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)

        // Projectile still moving
        caster.update(1000)
        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)

        // Projectile hit the target
        caster.update(1000)
        assertEquals(91, target.getTrait(HealthResourceTrait::class).get().health)
    }

    @Test
    fun `I should be able to cast spell with timed projectiles`() {
        target.position.x = 6f

        caster.getTrait(SpellCasterTrait::class).get().castSpell(3, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)

        // Projectile still moving
        caster.update(1000)
        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)

        // Projectile hit the target
        caster.update(1000)
        assertEquals(91, target.getTrait(HealthResourceTrait::class).get().health)
    }

    @Test
    fun `I should be able to cast spell with casting time`() {
        caster.getTrait<SpellCasterTrait>().get().castSpell(4, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)
        // 1 second casting
        caster.update(1000)
        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)
        // 2 seconds casting
        caster.update(1000)
        assertEquals(82, target.getTrait(HealthResourceTrait::class).get().health)
    }

    @Test
    fun `The cast time should break when moving`() {
        caster.addTrait(MovableTrait(caster))
        caster.getTrait<SpellCasterTrait>().get().castSpell(4, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)
        // 1 second casting
        caster.update(1000)
        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)

        // Walking
        caster.getTrait<MovableTrait>().get().move(Movement(Movement.MovementDirectionType.FORWARD, 0f))
        caster.update(100)
        assertEquals(Spell.SpellState.CANCELED, caster.getTrait<SpellCasterTrait>().get().getLastSpellCasted().get().state)
        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)

        // 2 more seconds casting
        caster.update(2000)
        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)
    }

    @Test
    fun `The cast time should not break when moving if has flag ALLOW_CAST_WHILE_MOVING`() {
        caster.addTrait(MovableTrait(caster))
        caster.getTrait<SpellCasterTrait>().get().castSpell(6, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)
        // 1 second casting
        caster.update(1000)
        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)

        // Walking
        caster.getTrait<MovableTrait>().get().move(Movement(Movement.MovementDirectionType.FORWARD, 0f))
        caster.update(100)
        assertEquals(Spell.SpellState.PREPARING, caster.getTrait<SpellCasterTrait>().get().getLastSpellCasted().get().state)
        assertEquals(100, target.getTrait(HealthResourceTrait::class).get().health)

        // 2 more seconds casting
        caster.update(2000)
        assertEquals(97, target.getTrait(HealthResourceTrait::class).get().health)
    }

    @Test
    fun `Ended spell should not be updated anymore`() {
        val counterTrait = CounterTrait()
        caster.addTrait(counterTrait)
        caster.getTrait(SpellCasterTrait::class).get().castSpell(5, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(1, counterTrait.counter)
        caster.update(100)
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

    override fun createSpellBook(): SpellBook =
        SpellBook(
            hashMapOf(
                1 to SpellInfo(SpellInfo.LaunchType.INSTANT, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(3))),
                2 to SpellInfo(SpellInfo.LaunchType.GHOST_PROJECTILES, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(3)), projectileSpeed = 3f),
                3 to SpellInfo(SpellInfo.LaunchType.TIMED_PROJECTILES, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(3)), projectileSpeed = 3f),
                4 to SpellInfo(SpellInfo.LaunchType.INSTANT, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(6)), CASTING_TIME_2_SECONDS),
                5 to SpellInfo(SpellInfo.LaunchType.INSTANT, arrayOf(CounterEffect.CounterEffectInfo())),
                6 to SpellInfo(SpellInfo.LaunchType.INSTANT, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(1)), CASTING_TIME_2_SECONDS, flags = EnumSet.of(SpellInfo.FLAGS.ALLOW_CAST_WHILE_MOVING)),
            ),
        )
}
