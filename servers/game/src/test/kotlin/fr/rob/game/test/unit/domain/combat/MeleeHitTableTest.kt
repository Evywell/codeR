package fr.rob.game.test.unit.domain.combat

import fr.rob.game.domain.combat.MeleeHitTable
import fr.rob.game.domain.combat.MeleeHitTableResult
import fr.rob.game.domain.entity.Position
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.RiggedDiceEngine
import fr.rob.game.test.unit.tools.TestCaseWithContainer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.test.inject

class MeleeHitTableTest : TestCaseWithContainer() {
    private val worldObjectBuilder: DummyWorldObjectBuilder by inject()

    @Test
    fun `As player, I should not be able to hit with crushing blow`() {
        // Arrange
        val rollEngine = RiggedDiceEngine(15_000)
        val hitTable = MeleeHitTable(rollEngine)

        val attacker = worldObjectBuilder.createPlayer()
        attacker.position = Position(0f, 0f, 0f, 0f)
        val victim = worldObjectBuilder.createUnit()
        victim.position = Position(0.5f, 0.5f, 0f, 0f)

        // Act
        val hitResult = hitTable.rollMeleeHit(attacker, victim)

        // Assert
        assertEquals(MeleeHitTableResult.HIT, hitResult)
    }

    @Test
    fun `As Unit, I should be able to hit with crushing blow`() {
        // Arrange
        val rollEngine = RiggedDiceEngine(15_000)
        val hitTable = MeleeHitTable(rollEngine)

        val attacker = worldObjectBuilder.createUnit()
        attacker.position = Position(0f, 0f, 0f, 0f)
        val victim = worldObjectBuilder.createPlayer()
        victim.position = Position(0.5f, 0.5f, 0f, 0f)

        // Act
        val hitResult = hitTable.rollMeleeHit(attacker, victim)

        // Assert
        assertEquals(MeleeHitTableResult.CRUSHING_BLOW, hitResult)
    }

    @Test
    fun `As Unit against another unit, I should be able to hit with crushing blow`() {
        // Arrange
        val rollEngine = RiggedDiceEngine(15_000)
        val hitTable = MeleeHitTable(rollEngine)

        val attacker = worldObjectBuilder.createUnit()
        attacker.position = Position(0f, 0f, 0f, 0f)
        val victim = worldObjectBuilder.createUnit()
        victim.position = Position(0.5f, 0.5f, 0f, 0f)

        // Act
        val hitResult = hitTable.rollMeleeHit(attacker, victim)

        // Assert
        assertEquals(MeleeHitTableResult.CRUSHING_BLOW, hitResult)
    }
}
