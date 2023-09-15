package fr.rob.game.test.unit.domain.spell

import fr.rob.game.domain.entity.ObjectManager
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.PositionNormalizer
import fr.rob.game.domain.entity.Unit
import fr.rob.game.domain.entity.behavior.HealthResourceTrait
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.spell.SpellBook
import fr.rob.game.domain.spell.SpellCasterTrait
import fr.rob.game.domain.spell.target.SingleObjectInInstanceSpellTarget
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SpellCasterTraitTest {
    @Test
    fun itShouldWork() {
        val instance = WorldBuilder.buildBasicWorld()
        val guidGenerator = ObjectGuidGenerator()
        val objectManager = ObjectManager(guidGenerator, PositionNormalizer())
        val unit = Unit(
            guidGenerator.fromGuidInfo(ObjectGuidGenerator.GuidInfo(ObjectGuid.LowGuid(1u, 0u), ObjectGuid.GUID_TYPE.GAME_OBJECT)),
            "The unit name",
            3,
        )
        unit.addTrait(SpellCasterTrait(unit, SpellBook()))

        val other = Unit(
            guidGenerator.fromGuidInfo(ObjectGuidGenerator.GuidInfo(ObjectGuid.LowGuid(2u, 0u), ObjectGuid.GUID_TYPE.GAME_OBJECT)),
            "Other",
            1,
        )
        other.addTrait(HealthResourceTrait(100))

        objectManager.addObjectToWorld(unit, instance, Position(0f, 0f, 0f, 0f))
        objectManager.addObjectToWorld(other, instance, Position(0f, 0f, 0f, 0f))

        unit.getTrait(SpellCasterTrait::class).get().castSpell(1, SingleObjectInInstanceSpellTarget(other.guid, unit.mapInstance))

        assertEquals(91, other.getTrait(HealthResourceTrait::class).get().health)
    }
}
