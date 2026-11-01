package fr.rob.game.test.unit.domain.combat

import fr.rob.game.behavior.CombatBehavior
import fr.rob.game.component.CombatComponent
import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class CombatTargetTest {
    private val worldBuilder = WorldBuilder()
    private val instance = worldBuilder.buildBasicWorld()
    private val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

    private val attacker = objectBuilder.createUnit().also { it.addComponent(CombatComponent()) }
    private val target = objectBuilder.createUnit()

    init {
        worldBuilder.addIntoInstance(attacker, instance, Position(0f, 0f, 0f, 0f))
        worldBuilder.addIntoInstance(target, instance, Position(20f, 0f, 0f, 0f))
    }

    @Test
    fun `the combat target is stored by guid`() {
        attacker.getComponent<CombatComponent>()!!.engageCombatWith(target)

        CombatBehavior.update(attacker, 0)

        assertEquals(target.guid, attacker.getComponent<CombatComponent>()!!.mainTargetGuid)
    }

    @Test
    fun `combat stops when the target leaves the instance`() {
        attacker.getComponent<CombatComponent>()!!.engageCombatWith(target)
        instance.grid.removeWorldObject(target)

        CombatBehavior.update(attacker, 0)

        assertNull(attacker.getComponent<CombatComponent>()!!.mainTargetGuid)
    }
}
