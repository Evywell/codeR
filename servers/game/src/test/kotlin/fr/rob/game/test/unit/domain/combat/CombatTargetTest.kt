package fr.rob.game.test.unit.domain.combat

import fr.rob.game.behavior.CombatBehavior
import fr.rob.game.behavior.ObjectSheetBehavior
import fr.rob.game.component.CombatComponent
import fr.rob.game.component.resource.HealthComponent
import fr.rob.game.entity.Position
import fr.rob.game.entity.guid.ObjectGuidGenerator
import fr.rob.game.entity.scheduler.TaskGroup
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.RiggedDiceEngine
import fr.rob.game.test.unit.tools.WorldBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CombatTargetTest {
    private val worldBuilder = WorldBuilder()
    private val instance = worldBuilder.buildBasicWorld()
    private val objectBuilder = DummyWorldObjectBuilder(ObjectGuidGenerator())

    private val attacker = objectBuilder.createUnit().also {
        it.addComponent(CombatComponent())
        it.addBehavior(CombatBehavior)
        it.addBehavior(ObjectSheetBehavior(RiggedDiceEngine()))
    }
    private val target = objectBuilder.createUnit().also { it.addComponent(HealthComponent(100)) }
    private val health = target.getComponent<HealthComponent>()!!

    init {
        // Attacker faces +x, target 1 unit in front
        worldBuilder.addIntoInstance(attacker, instance, Position(0f, 0f, 0f, 0f))
        worldBuilder.addIntoInstance(target, instance, Position(1f, 0f, 0f, 0f))
    }

    @Test
    fun `the combat target is stored by guid`() {
        CombatBehavior.engage(attacker, target)

        assertEquals(target.guid, attacker.getComponent<CombatComponent>()!!.mainTargetGuid)
    }

    @Test
    fun `the first swing is immediate, then once per weapon speed`() {
        CombatBehavior.engage(attacker, target)

        attacker.onUpdate(0)
        val afterFirstSwing = health.value
        assertTrue(afterFirstSwing < 100)

        attacker.onUpdate(CombatComponent.MAIN_HAND_WEAPON_SPEED_MS - 1)
        assertEquals(afterFirstSwing, health.value)

        attacker.onUpdate(1)
        assertTrue(health.value < afterFirstSwing)
    }

    @Test
    fun `switching target keeps the cadence`() {
        val otherTarget = objectBuilder.createUnit().also { it.addComponent(HealthComponent(100)) }
        worldBuilder.addIntoInstance(otherTarget, instance, Position(1f, 0.1f, 0f, 0f))
        CombatBehavior.engage(attacker, target)
        attacker.onUpdate(0)

        CombatBehavior.engage(attacker, otherTarget)
        attacker.onUpdate(0)

        assertEquals(100, otherTarget.getComponent<HealthComponent>()!!.value)
    }

    @Test
    fun `a ready swing hits as soon as the target comes back in range`() {
        CombatBehavior.engage(attacker, target)
        target.setPosition(10f, 0f, 0f, 0f)

        attacker.onUpdate(CombatComponent.MAIN_HAND_WEAPON_SPEED_MS)
        assertEquals(100, health.value)

        target.setPosition(1f, 0f, 0f, 0f)
        attacker.onUpdate(20)

        assertTrue(health.value < 100)
    }

    @Test
    fun `engaging twice keeps a single swing`() {
        CombatBehavior.engage(attacker, target)
        CombatBehavior.engage(attacker, target)

        attacker.onUpdate(0)
        val afterSwing = health.value
        attacker.onUpdate(0)

        assertTrue(afterSwing < 100)
        assertEquals(afterSwing, health.value)
    }

    @Test
    fun `disengaging stops the swings`() {
        CombatBehavior.engage(attacker, target)
        CombatBehavior.disengage(attacker)

        attacker.onUpdate(CombatComponent.MAIN_HAND_WEAPON_SPEED_MS)

        assertEquals(100, health.value)
        assertFalse(attacker.scheduler.isScheduled(TaskGroup.COMBAT_SWING))
    }

    @Test
    fun `combat stops when the target leaves the instance`() {
        CombatBehavior.engage(attacker, target)
        instance.grid.removeWorldObject(target)

        attacker.onUpdate(0)

        assertNull(attacker.getComponent<CombatComponent>()!!.mainTargetGuid)
        assertFalse(attacker.scheduler.isScheduled(TaskGroup.COMBAT_SWING))
    }

    @Test
    fun `removing the attacker from the instance cancels its swings`() {
        CombatBehavior.engage(attacker, target)

        attacker.scheduleRemoveFromInstance()

        assertFalse(attacker.scheduler.isScheduled(TaskGroup.COMBAT_SWING))
    }
}
