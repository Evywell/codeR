package fr.rob.game.test.unit.domain.combat

import fr.rob.game.domain.combat.MeleeHitTable
import fr.rob.game.domain.combat.MeleeHitTableResult
import fr.rob.game.domain.entity.Unit
import fr.rob.game.domain.entity.guid.ObjectGuid
import fr.rob.game.domain.entity.guid.ObjectGuidGenerator
import fr.rob.game.test.unit.tools.DummyPlayerBuilder
import fr.rob.game.test.unit.tools.RiggedDiceEngine
import fr.rob.game.test.unit.tools.WithContainerTestCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.test.inject

class MeleeHitTableTest : WithContainerTestCase() {
    private val playerBuilder: DummyPlayerBuilder by inject()
    private val guidGenerator: ObjectGuidGenerator by inject()

    @Test
    fun `As player, I should not be able to hit with crushing blow`() {
        // Arrange
        val rollEngine = RiggedDiceEngine(15_000)
        val hitTable = MeleeHitTable(rollEngine)

        val attacker = playerBuilder.createPlayer()
        val victim = Unit(
            guidGenerator.fromGuidInfo(ObjectGuidGenerator.GuidInfo(ObjectGuid.LowGuid(1u, 0u), ObjectGuid.GUID_TYPE.GAME_OBJECT)),
            "Unit to hit",
            1
        )

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

        val victim = playerBuilder.createPlayer()
        val attacker = Unit(
            guidGenerator.fromGuidInfo(ObjectGuidGenerator.GuidInfo(ObjectGuid.LowGuid(1u, 0u), ObjectGuid.GUID_TYPE.GAME_OBJECT)),
            "Unit to hit",
            1
        )

        // Act
        val hitResult = hitTable.rollMeleeHit(attacker, victim)

        // Assert
        assertEquals(MeleeHitTableResult.CRUSHING_BLOW, hitResult)
    }
}
