package fr.rob.game.test.unit.domain.combat.table

import fr.rob.game.combat.HitTableRoll
import fr.rob.game.combat.MeleeHitTableResult
import fr.rob.game.combat.hittable.DodgeMeleeStep
import fr.rob.game.entity.Position
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.HitTableStepTester
import fr.rob.game.test.unit.tools.TestCaseWithContainer
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.koin.test.inject
import kotlin.math.PI
import kotlin.test.assertEquals

class DodgeMeleeStepTest : TestCaseWithContainer() {
    private val worldObjectBuilder: DummyWorldObjectBuilder by inject()

    @Test
    fun `Dodge cannot happened when attacker is behind the target`() {
        // Arrange
        val attacker = worldObjectBuilder.createUnit()
        attacker.position = Position(0f, 0f, 0f, 0f) // Look position: --->
        val victim = worldObjectBuilder.createUnit()
        victim.position = Position(0.1f, 0f, 0f, 0f) // Look position: --->

        val stepTester = HitTableStepTester()
        val hitRoll = HitTableRoll(attacker, victim, 1)
        val step = DodgeMeleeStep()

        // Expect
        assertThrows(EndOfHitTableException::class.java) {
            // Act
            step.execute(hitRoll, stepTester)
        }
    }

    @Test
    fun `Dodge can happened when attacker is NOT behind the target`() {
        // Arrange
        val attacker = worldObjectBuilder.createUnit()
        attacker.position = Position(0f, 0f, 0f, 0f) // Look position: --->
        val victim = worldObjectBuilder.createUnit()
        victim.position = Position(0.1f, 0f, 0f, PI.toFloat()) // Look position: <---

        val stepTester = HitTableStepTester()
        val hitRoll = HitTableRoll(attacker, victim, 1)
        val step = DodgeMeleeStep()

        // Act
        val result = step.execute(hitRoll, stepTester)

        // Assert
        assertEquals(MeleeHitTableResult.DODGE, result)
    }
}
