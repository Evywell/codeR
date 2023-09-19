package fr.rob.game.test.unit.domain.spell

import fr.rob.game.domain.entity.WorldObject
import fr.rob.game.domain.spell.Spell
import fr.rob.game.domain.spell.SpellBook
import fr.rob.game.domain.spell.SpellCasterTrait
import fr.rob.game.domain.spell.SpellInfo
import fr.rob.game.domain.spell.effect.EffectFromSpellInterface
import fr.rob.game.domain.spell.effect.EffectInterface
import fr.rob.game.domain.spell.effect.SpellEffectInfo
import fr.rob.game.domain.spell.effect.SpellEffectTypeEnum
import fr.rob.game.domain.spell.target.SpellTargetParameter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SpellBookTest : SpellCasterEnvironmentBaseTest() {
    @Test
    fun `Ended spell should not be updated anymore`() {
        val counterTrait = CounterTrait()
        caster.addTrait(counterTrait)
        caster.getTrait(SpellCasterTrait::class).get().castSpell(1, SpellTargetParameter(target.guid, caster.mapInstance))

        assertEquals(1, counterTrait.counter)
        spellBook.update(100)
        assertEquals(1, counterTrait.counter)
    }

    override fun createSpellBook(): SpellBook = SpellBook(
        hashMapOf(
            1 to SpellInfo(SpellInfo.LaunchType.INSTANT, arrayOf(CounterEffect.CounterEffectInfo())),
        ),
    )

    private class CounterEffect(private val caster: WorldObject) : EffectInterface {
        class CounterEffectInfo : SpellEffectInfo(SpellEffectTypeEnum.INSTANT_DAMAGE), EffectFromSpellInterface {
            override fun createEffectFromSpell(spell: Spell): EffectInterface = CounterEffect(spell.caster)
        }

        override fun cast() {
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
}
