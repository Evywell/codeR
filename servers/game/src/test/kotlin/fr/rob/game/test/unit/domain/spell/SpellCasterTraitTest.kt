package fr.rob.game.test.unit.domain.spell

import fr.rob.game.domain.entity.behavior.HealthResourceTrait
import fr.rob.game.domain.spell.SpellBook
import fr.rob.game.domain.spell.SpellCasterTrait
import fr.rob.game.domain.spell.SpellInfo
import fr.rob.game.domain.spell.effect.InstantDamageEffect
import fr.rob.game.domain.spell.target.SpellTargetParameter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
    fun `I should be able to cas spell with timed projectiles`() {
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

    override fun createSpellBook(): SpellBook =
        SpellBook(
            hashMapOf(
                1 to SpellInfo(SpellInfo.LaunchType.INSTANT, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(3))),
                2 to SpellInfo(SpellInfo.LaunchType.GHOST_PROJECTILES, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(3)), 3f),
                3 to SpellInfo(SpellInfo.LaunchType.TIMED_PROJECTILES, arrayOf(InstantDamageEffect.InstantDamageEffectInfo(3)), 3f),
            ),
        )
}
