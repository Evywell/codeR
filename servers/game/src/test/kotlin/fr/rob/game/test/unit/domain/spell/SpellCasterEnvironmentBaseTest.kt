package fr.rob.game.test.unit.domain.spell

import fr.rob.game.domain.entity.ObjectManager
import fr.rob.game.domain.entity.Position
import fr.rob.game.domain.entity.PositionNormalizer
import fr.rob.game.domain.entity.Unit
import fr.rob.game.domain.entity.behavior.ObjectSheetTrait
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.domain.spell.SpellBook
import fr.rob.game.domain.spell.SpellCasterTrait
import fr.rob.game.test.unit.tools.RiggedDiceEngine
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.BeforeEach

abstract class SpellCasterEnvironmentBaseTest {
    protected lateinit var caster: Unit
    protected lateinit var target: Unit
    protected lateinit var spellBook: SpellBook
    protected lateinit var targetRiggedDiceEngine: RiggedDiceEngine

    @BeforeEach
    fun setUp() {
        val instance = WorldBuilder.buildBasicWorld()
        val guidGenerator = ObjectGuidGenerator()
        val objectManager = ObjectManager(guidGenerator, PositionNormalizer())
        spellBook = createSpellBook()
        targetRiggedDiceEngine = RiggedDiceEngine()

        caster = Unit(
            guidGenerator.fromGuidInfo(ObjectGuidGenerator.GuidInfo(ObjectGuid.LowGuid(1u, 0u), ObjectGuid.GUID_TYPE.GAME_OBJECT)),
            "The unit name",
            3,
        )
        caster.addTrait(SpellCasterTrait(caster, spellBook))

        target = Unit(
            guidGenerator.fromGuidInfo(ObjectGuidGenerator.GuidInfo(ObjectGuid.LowGuid(2u, 0u), ObjectGuid.GUID_TYPE.GAME_OBJECT)),
            "Other",
            1,
        )
        target.addTrait(ObjectSheetTrait(target, 100, targetRiggedDiceEngine))

        objectManager.addObjectToWorld(caster, instance, Position(0f, 0f, 0f, 0f))
        objectManager.addObjectToWorld(target, instance, Position(3f, 0f, 0f, 0f))
    }

    abstract fun createSpellBook(): SpellBook
}
