package fr.rob.game.test.unit.domain.spell

import fr.rob.game.behavior.ObjectSheetBehavior
import fr.rob.game.component.resource.HealthComponent
import fr.rob.game.entity.ObjectManager
import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuid
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.spell.SpellBook
import fr.rob.game.spell.SpellCasterTrait
import fr.rob.game.test.unit.tools.RiggedDiceEngine
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.BeforeEach
import fr.rob.game.entity.Unit as WorldUnit

abstract class SpellCasterEnvironmentBaseTest {
    protected lateinit var caster: WorldUnit
    protected lateinit var target: WorldUnit
    protected val targetRiggedDiceEngine = RiggedDiceEngine()
    protected val instance = WorldBuilder.buildBasicWorld()

    private lateinit var spellBook: SpellBook
    private val guidGenerator = ObjectGuidGenerator()
    private val objectManager = ObjectManager(guidGenerator)
    private var entryGuid: UInt = 1u

    @BeforeEach
    fun setUp() {
        spellBook = createSpellBook()

        caster = createUnit("The unit name", 3, Position(0f, 0f, 0f, 0f))
        caster.addTrait(SpellCasterTrait(caster, spellBook))

        target = createUnit("Other", 1, Position(3f, 0f, 0f, 0f))
    }

    abstract fun createSpellBook(): SpellBook

    protected fun createUnit(name: String, level: Int, position: Position): WorldUnit {
        val unitToCreate = WorldUnit(
            guidGenerator.fromGuidInfo(ObjectGuidGenerator.GuidInfo(ObjectGuid.LowGuid(entryGuid++, 0u), ObjectGuid.GUID_TYPE.GAME_OBJECT)),
            name,
            level,
        )
        unitToCreate.addComponent(HealthComponent(100))
        unitToCreate.addBehavior(ObjectSheetBehavior(targetRiggedDiceEngine))
        unitToCreate.addIntoInstance(instance, position)

        return unitToCreate
    }
}
