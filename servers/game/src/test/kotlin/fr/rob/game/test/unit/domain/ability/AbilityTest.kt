package fr.rob.game.test.unit.domain.ability

import fr.rob.game.ability.Ability
import fr.rob.game.ability.AbilityInfo
import fr.rob.game.ability.AbilityRequirements
import fr.rob.game.ability.AbilityTargetParameter
import fr.rob.game.ability.AbilityType
import fr.rob.game.ability.launch.InstantLaunchInfo
import fr.rob.game.ability.resource.ManaResourceType
import fr.rob.game.entity.behavior.HoldPowerResourcesTrait
import fr.rob.game.entity.power.ManaPower
import fr.rob.game.test.unit.tools.DummyWorldObjectBuilder
import fr.rob.game.test.unit.tools.TestCaseWithContainer
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.koin.test.inject

class AbilityTest : TestCaseWithContainer() {
    private val worldObjectBuilder: DummyWorldObjectBuilder by inject()

    @Test
    fun `Try using ability without enough mana should fail`() {
        // Arrange
        val source = worldObjectBuilder.createPlayer()
        source.addTrait(HoldPowerResourcesTrait(ManaPower(10)))

        val ability =
            Ability(
                info =
                    AbilityInfo(
                        identifier = 1,
                        type = AbilityType.MAGICAL,
                        abilityRequirement = AbilityRequirements(arrayOf(ManaResourceType(100))),
                        launchInfo = InstantLaunchInfo(),
                        castingTimeMs = AbilityInfo.INSTANT_CASTING_TIME,
                    ),
                source = source,
                target = AbilityTargetParameter(null, source),
            )

        // Act
        ability.use()

        // Assert
        assertTrue(ability.failed())
        assertTrue(ability.isDone())
        assertFalse(ability.isInProgress())
    }

    @Test
    fun `Use ability with enough mana should leads to ability done without failure`() {
        // Arrange
        val source = worldObjectBuilder.createPlayer()
        source.addTrait(HoldPowerResourcesTrait(ManaPower(100)))

        val ability =
            Ability(
                info =
                    AbilityInfo(
                        identifier = 1,
                        type = AbilityType.MAGICAL,
                        abilityRequirement = AbilityRequirements(arrayOf(ManaResourceType(100))),
                        launchInfo = InstantLaunchInfo(),
                        castingTimeMs = AbilityInfo.INSTANT_CASTING_TIME,
                    ),
                source = source,
                target = AbilityTargetParameter(null, source),
            )

        // Act
        ability.use()

        // Assert
        assertTrue(ability.isDone())
        assertFalse(ability.failed())
        assertFalse(ability.isInProgress())
    }

    @Test
    fun `Use ability with non instant casting time`() {
        // Arrange
        val source = worldObjectBuilder.createPlayer()
        source.addTrait(HoldPowerResourcesTrait(ManaPower(100)))

        val ability =
            Ability(
                info =
                    AbilityInfo(
                        identifier = 1,
                        type = AbilityType.MAGICAL,
                        abilityRequirement = AbilityRequirements(arrayOf(ManaResourceType(100))),
                        launchInfo = InstantLaunchInfo(),
                        castingTimeMs = 1000,
                    ),
                source = source,
                target = AbilityTargetParameter(null, source),
            )

        // Act & Assert
        ability.use()

        assertTrue(ability.isInProgress())
        assertFalse(ability.isDone())
        assertFalse(ability.failed())

        ability.resume(500)

        assertTrue(ability.isInProgress())
        assertFalse(ability.isDone())
        assertFalse(ability.failed())

        ability.resume(500)

        assertFalse(ability.isInProgress())
        assertTrue(ability.isDone())
        assertFalse(ability.failed())
    }
}
